package com.smartboard.Utils;

import com.smartboard.exceptions.UserException;
import com.smartboard.models.*;
import com.smartboard.models.interfaces.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * database communication class that provide static methods to interact with the application database.
 * it performs basic CRUD transactions
 */
public class DBManager {

    private static final String url = "jdbc:sqlite:src/smartboard.db3";

    /**
     * enables foreign keys
     *
     * @param conn the connection
     * @throws SQLException
     */
    private static void foreignKeysOn(Connection conn) throws SQLException {
        conn.prepareStatement("pragma foreign_keys = on;").execute();
    }


    /**
     * verifies a user-password combination
     *
     * @param username the username
     * @param password the password
     * @return true if the username-password is valid, false if invalid or an error with the database
     */
    public static boolean authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(url)) {
            foreignKeysOn(conn);
            PreparedStatement statement = conn.prepareStatement("select password_hash from logins where username = ?");
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            String pwdHash = result.getString("password_hash");

            return BCrypt.checkpw(password, pwdHash);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * creates a user, login, default project and default columns in the database
     *
     * @param user the user to create
     * @throws UserException is the username is not available or the database transaction fails
     */
    public static void createUser(User user) throws UserException {

        try (Connection conn = DriverManager.getConnection(url)) {

            PreparedStatement userStatement;
            userStatement = conn.prepareStatement(
                    "insert into users (username, firstname, lastname, profile_picture_path) values (?,?,?,?);");
            userStatement.setString(1, user.getUsername());
            userStatement.setString(2, user.getFirstName());
            userStatement.setString(3, user.getLastName());
            userStatement.setString(4, user.getProfilePicturePath());

            PreparedStatement loginStatement = conn.prepareStatement(
                    "insert into logins (username, password_hash) VALUES (?,?)");
            loginStatement.setString(1, user.getUsername());
            loginStatement.setString(2, user.getLogin().getPasswordHash());

            userStatement.execute();
            loginStatement.execute();

            createWorkspace(user);
            return;

        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("unique constraint failed: users.username"))
                throw new UserException("> Username already taken, please try a different Username");
        }
        throw new UserException("> Something went wrong, please ty again");

    }

    /**
     * creates a workspace in the database with a default project and columns
     *
     * @param user the owner of the workspace
     * @return the new workspace object
     */
    public static Workspace createWorkspace(User user) {
        WorkspaceImpl workSpace;
        Project defaultProject;
        try (Connection conn = DriverManager.getConnection(url)) {

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO workspaces (username) values (?); ");
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.execute();

            ResultSet resultSet = conn.prepareStatement(
                    "SELECT id FROM workspaces ORDER BY rowid DESC LIMIT 1;").executeQuery();
            resultSet.next();
            workSpace = new WorkspaceImpl(resultSet.getInt(1), user);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        defaultProject = new ProjectImpl("My Project", workSpace);
        defaultProject.setColumns(addDefaultColumns(defaultProject));

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.prepareStatement("update workspaces set default_project = " +
                    defaultProject.getId() + " where id = " + workSpace.getId()).execute();

            workSpace.setDefaultProject(defaultProject);
            return workSpace;

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * adds a project to the database
     *
     * @param project the project to add
     * @return the database autogenerated project id or -1 if an error occurred
     */
    public static int createProject(Project project) {
        try (Connection conn = DriverManager.getConnection(url)) {

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO projects (name, workspace_id) values (?,?) ");
            preparedStatement.setString(1, project.getName());
            preparedStatement.setInt(2, project.getWorkSpace().getId());
            preparedStatement.execute();

            ResultSet resultSet = conn.prepareStatement(
                    "SELECT id FROM projects ORDER BY rowid DESC LIMIT 1").executeQuery();
            resultSet.next();

            return resultSet.getInt(1);


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Adds a column to the database
     *
     * @param column the column to add to the database
     * @return the column id generated by the database or -1 if there is an error
     */
    public static int createColumn(Column column) {
        try (Connection conn = DriverManager.getConnection(url)) {


            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO columns (name, project_id) values (?,?); ");
            preparedStatement.setString(1, column.getName());
            preparedStatement.setInt(2, column.getProject().getId());
            preparedStatement.execute();

            ResultSet resultSet = conn.prepareStatement(
                    "SELECT id FROM columns ORDER BY rowid DESC LIMIT 1;").executeQuery();
            resultSet.next();
            return resultSet.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Generates a list of columns with names To do, Doing, Complete
     *
     * @param project the column's parent
     * @return
     */
    private static List<Column> addDefaultColumns(Project project) {

        List<Column> defaultColumns = Arrays.asList(
                new ColumnImpl("To Do", project),
                new ColumnImpl("Doing", project),
                new ColumnImpl("Completed", project)
        );
        return defaultColumns;
    }

    /**
     * adds a task to the database, retrieves an id from the database and sets it on the task object
     *
     * @param task the task to add to the database
     * @return the autogenerated task id or -1 if there is an error
     */
    public static int createTask(Task task) {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "insert into tasks (name, description, duedate, state, column_id, \"index\") values (?,?,?,?,?,?);");
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setDate(3, new Date(task.getDueDate().getTimeInMillis()));
            preparedStatement.setString(4, task.getState().name());
            preparedStatement.setInt(5, task.getColumn().getId());
            preparedStatement.setInt(6, task.getIndex());
            preparedStatement.execute();

            ResultSet resultSet = conn.prepareStatement(
                    "SELECT id FROM tasks ORDER BY rowid DESC LIMIT 1;").executeQuery();
            resultSet.next();
            return resultSet.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * get a user from database
     *
     * @param username
     * @return
     */
    public static User readUser(String username) {
        User user = null;
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from users where username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new UserImpl(
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("username"),
                        resultSet.getString("profile_picture_path"));
            }
        } catch (SQLException | UserException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * gets the user's workspace
     *
     * @param user
     * @return
     */
    public static WorkspaceImpl readWorkspace(User user) {
        WorkspaceImpl workSpace = null;
        int defaultProject;
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from workspaces where username = ?");
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                workSpace = new WorkspaceImpl(resultSet.getInt("id"), user);
                workSpace.setProjects(readProjects(workSpace));
                defaultProject = resultSet.getInt("default_project");
                workSpace.getProjects()
                        .stream()
                        .filter(c -> c.getId() == defaultProject)
                        .findFirst()
                        .ifPresent(workSpace::setDefaultProject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workSpace;

    }

    /**
     * get the workspace's projects
     *
     * @param workSpace
     * @return
     */
    public static List<Project> readProjects(WorkspaceImpl workSpace) {
        List<Project> projects = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from projects where workspace_id = ?");
            preparedStatement.setInt(1, workSpace.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Project project = new ProjectImpl(resultSet.getString("name"), workSpace, resultSet.getInt("id"));
                project.setColumns(readColumns(project));
                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    /**
     * get the project's columns
     *
     * @param project
     * @return
     */
    public static List<Column> readColumns(Project project) {
        List<Column> columns = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from columns where project_id = ?");
            preparedStatement.setInt(1, project.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                Column column = new ColumnImpl(id, name, project);
                column.setTasks(readTasks(column));

                columns.add(column);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return columns;
    }

    /**
     * get the column's tasks
     *
     * @param column
     * @return
     */
    public static List<Task> readTasks(Column column) {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from tasks where column_id = ?");
            preparedStatement.setInt(1, column.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(resultSet.getDate("duedate").getTime());
                Task task = new TaskImpl(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        calendar,
                        TaskState.valueOf(resultSet.getString("state")),
                        column,
                        resultSet.getInt("index")
                );
                task.setListItems(readListItems(task));

                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     * get the task's list items
     *
     * @param task
     * @return
     */
    public static List<ListItem> readListItems(Task task) {
        List<ListItem> items = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from list_items where task_id = ?");
            preparedStatement.setInt(1, task.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ListItem item = new ListItemImpl();
                item.setTask(task);
                item.setId(resultSet.getInt("id"));
                item.setDescription(resultSet.getString("description"));
                item.setCompleted(resultSet.getBoolean("is_completed"));

                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static boolean deleteUser(User user) {
        return deleteRow(user);
    }

    public static boolean deleteWorkspace(Workspace workspace) {
        return deleteRow(workspace);
    }

    public static boolean deleteProject(Project project) {
        return deleteRow(project);
    }

    public static boolean deleteColumn(Column column) {
        return deleteRow(column);
    }

    public static boolean deleteTask(Task task) {
        return deleteRow(task);
    }

    /**
     * generic method for deleting a row that implements the identifiable interface
     *
     * @param model
     * @param <T    ? implements identifiable>
     * @return
     */
    private static <T> boolean deleteRow(T model) {
        // Check if operation can be performed
        if (!(model instanceof Identifiable))
            return false;

        // get class name and add 's' to match DB table names
        String tableName =
                model instanceof Workspace ?
                        "workspaces" :
                        model instanceof Project ?
                                "projects" :
                                model instanceof Column ?
                                        "columns" :
                                        model instanceof Task ?
                                                "tasks" :
                                                model instanceof ListItem ?
                                                        "listitems" :
                                                        null;

        if (tableName == null)
            return false;


        try (Connection conn = DriverManager.getConnection(url)) {

            conn.prepareStatement("pragma foreign_keys = on;").execute();

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "delete from " + tableName + " where id = ?");
            preparedStatement.setInt(1, ((Identifiable) model).getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * updates task on the database
     *
     * @param task the task to update
     */
    public static boolean updateTask(Task task) {
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.prepareStatement("pragma foreign_keys = on;").execute();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "update tasks set name = ?, description = ?, duedate = ?, state = ?, column_id = ?, \"index\" = ? where id = ? "
            );
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setDate(3, new Date(task.getDueDate().getTimeInMillis()));
            preparedStatement.setString(4, task.getState().name());
            preparedStatement.setInt(5, task.getColumn().getId());
            preparedStatement.setInt(6, task.getIndex());
            preparedStatement.setInt(7, task.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * updates the column's name
     *
     * @param column
     * @return
     */
    public static boolean updateColumn(Column column) {
        try (Connection conn = DriverManager.getConnection(url)) {
            foreignKeysOn(conn);
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "update columns set name = ? where id = ? "
            );
            preparedStatement.setString(1, column.getName());
            preparedStatement.setInt(2, column.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * updates the project's name
     *
     * @param project
     * @return
     */
    public static boolean updateProject(Project project) {
        try (Connection conn = DriverManager.getConnection(url)) {
            foreignKeysOn(conn);
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "update projects set name = ? where id = ? "
            );
            preparedStatement.setString(1, project.getName());
            preparedStatement.setInt(2, project.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * updates user and password information
     *
     * @param username
     * @param password
     * @param firstname
     * @param lastname
     * @param profilePicPath
     * @return
     * @throws UserException if firstname, lastname or password are empty
     */
    public static boolean updateUser(String username, String password, String firstname, String lastname, String profilePicPath) throws UserException {
        String errorMessage = "";
        if (Utils.NullOrEmpty(firstname))
            errorMessage += "> First Name can not be empty\n";
        if (Utils.NullOrEmpty(lastname))
            errorMessage += "> Last Name can not be empty\n";
        if (Utils.NullOrEmpty(password))
            errorMessage += "> Password can not be empty\n";

        if (!errorMessage.isBlank())
            throw new UserException(errorMessage.substring(0, errorMessage.length() - 1));

        String pwdHash = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = DriverManager.getConnection(url)) {
            foreignKeysOn(conn);
            PreparedStatement userStatement = conn.prepareStatement(
                    "update users set firstname = ?, lastname = ?, profile_picture_path = ? where username = ?;");
            userStatement.setString(1, firstname);
            userStatement.setString(2, lastname);
            userStatement.setString(3, profilePicPath);
            userStatement.setString(4, username);

            PreparedStatement loginStatement = conn.prepareStatement(
                    "update logins set password_hash = ? where username = ?;");
            loginStatement.setString(2, pwdHash);
            loginStatement.setString(1, username);

            userStatement.execute();
            loginStatement.execute();

            return true;

        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("unique constraint failed: users.username"))
                throw new UserException("> Username already taken, please try a different Username");
        }
        throw new UserException("> Something went wrong, please ty again");
    }

    /**
     * updates user information
     *
     * @param user the user to update
     * @return
     */
    public static boolean updateUser(User user) {
        try (Connection conn = DriverManager.getConnection(url)) {
            foreignKeysOn(conn);
            PreparedStatement userStatement = conn.prepareStatement(
                    "update users set firstname = ?, lastname = ?, profile_picture_path = ? where username = ?;");
            userStatement.setString(1, user.getFirstName());
            userStatement.setString(2, user.getLastName());
            userStatement.setString(3, user.getProfilePicturePath());
            userStatement.setString(4, user.getUsername());

            userStatement.execute();

            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * update the given workspace default project id
     *
     * @param workspace
     * @return
     */
    public static boolean updateWorkspace(WorkspaceImpl workspace) {
        try (Connection conn = DriverManager.getConnection(url)) {
            foreignKeysOn(conn);
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "update workspaces set default_project = ? where id = ?; ");
            if (workspace.getDefaultProject() == null) {
                preparedStatement.setNull(1, Types.INTEGER);
            } else {
                preparedStatement.setInt(1, workspace.getDefaultProject().getId());
            }
            preparedStatement.setInt(2, workspace.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateLogin(Login login) {
        try (Connection conn = DriverManager.getConnection(url)) {
            foreignKeysOn(conn);
            PreparedStatement loginStatement = conn.prepareStatement(
                    "update logins set password_hash = ? where username = ?;");
            loginStatement.setString(2, login.getPasswordHash());
            loginStatement.setString(1, login.getUsername());

            loginStatement.execute();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}