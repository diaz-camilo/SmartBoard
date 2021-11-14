package com.smartboard.Utils;

import com.smartboard.exceptions.UserException;
import com.smartboard.models.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DBManager {

    private static final String url = "jdbc:sqlite:src/smartboard.db3";

    private static void getConnection() {
        try (Connection conn = DriverManager.getConnection(url)) {

            var usersStatement = conn.prepareStatement("select * from users");
            var usersResult = usersStatement.executeQuery();

            List<User> users = new ArrayList<>();
            while (usersResult.next()) {
                String username = usersResult.getString("username");
                String firstname = usersResult.getString("firstname");
                String lastname = usersResult.getString("lastname");
                String picturePath = usersResult.getString("profile_picture_path");
                System.out.printf("%-15s %-15s %-15s %-15s\n", username, firstname, lastname, picturePath);
            }


            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean AuthenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(url)) {

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

    public static User RegisterUser(String username, String password, String firstname, String lastname, String profilePicPath) throws UserException {
        String errorMessage = "";
        if (Utils.NullOrEmpty(firstname))
            errorMessage += "> First Name can not be empty\n";
        if (Utils.NullOrEmpty(lastname))
            errorMessage += "> Last Name can not be empty\n";
        if (Utils.NullOrEmpty(username))
            errorMessage += "> Username can not be empty\n";
        if (Utils.NullOrEmpty(password))
            errorMessage += "> Password can not be empty\n";

        if (!errorMessage.isBlank())
            throw new UserException(errorMessage.substring(0, errorMessage.length() - 1));

        String pwdHash = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = DriverManager.getConnection(url)) {

            PreparedStatement userStatement;
            userStatement = conn.prepareStatement(
                    "insert into users (username, firstname, lastname, profile_picture_path) values (?,?,?,?);");
            userStatement.setString(1, username);
            userStatement.setString(2, firstname);
            userStatement.setString(3, lastname);
            userStatement.setString(4, profilePicPath);

            PreparedStatement loginStatement = conn.prepareStatement(
                    "insert into logins (username, password_hash) VALUES (?,?)");
            loginStatement.setString(1, username);
            loginStatement.setString(2, pwdHash);

            userStatement.execute();
            loginStatement.execute();

            User user = new User();
            user.setUsermane(username);
            user.setLastName(lastname);
            user.setFirstName(firstname);
            addWorkspace(username);


            return user;

        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("unique constraint failed: users.username"))
                throw new UserException("> Username already taken, please try a different Username");
        }
        throw new UserException("> Something went wrong, please ty again");

    }

    /**
     * Adds a new workspace to the database with a default project
     *
     * @param username the owner of the workspace
     * @return the now workspace object
     */
    public static Workspace addWorkspace(String username) {
        Workspace workSpace = new Workspace();
        workSpace.setUsername(username);
        Project defaultProject;
        try (Connection conn = DriverManager.getConnection(url)) {

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO workspaces (username) values (?); ");
            preparedStatement.setString(1, username);
            preparedStatement.execute();

            ResultSet resultSet = conn.prepareStatement(
                    "SELECT id FROM workspaces ORDER BY rowid DESC LIMIT 1;").executeQuery();
            resultSet.next();
            workSpace.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        defaultProject = addDefaultProject(workSpace.getId(), "My Project");

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
     * Adds a new project to the database with the default values
     *
     * @param workspaceId the parent workspace id for the project
     * @param name        the name of the project
     * @return the project object
     */
    public static Project addDefaultProject(int workspaceId, String name) {
        try (Connection conn = DriverManager.getConnection(url)) {

            Project defaultProject = new Project();
            defaultProject.setName(name);

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO projects (name, workspace_id) values (?,?) ");
            preparedStatement.setString(1, defaultProject.getName());
            preparedStatement.setInt(2, workspaceId);
            preparedStatement.execute();

            ResultSet resultSet = conn.prepareStatement(
                    "SELECT id FROM projects ORDER BY rowid DESC LIMIT 1").executeQuery();
            resultSet.next();

            defaultProject.setId(resultSet.getInt(1));
            conn.close();
            defaultProject.setColumns(addDefaultColumns(defaultProject.getId()));

            return defaultProject;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds an empty project to the database
     *
     * @param workspaceId the parent workspace id
     * @param name        the name of the project
     * @return a project object with name, id and columns set
     */
    public static Project addProject(int workspaceId, String name) {
        try (Connection conn = DriverManager.getConnection(url)) {

            Project project = new Project();
            project.setName(name);
            project.setColumns(new ArrayList<>());

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO projects (name, workspace_id) values (?,?) ");
            preparedStatement.setString(1, project.getName());
            preparedStatement.setInt(2, workspaceId);
            preparedStatement.execute();

            ResultSet resultSet = conn.prepareStatement(
                    "SELECT id FROM projects ORDER BY rowid DESC LIMIT 1").executeQuery();
            resultSet.next();

            project.setId(resultSet.getInt(1));

            return project;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Generates a list of columns with names To do, Doing, Complete
     *
     * @param projectId
     * @return
     */
    public static List<Column> addDefaultColumns(int projectId) {
        List<Column> defaultColumns = Arrays.asList(
                addColumn(projectId, "To Do"),
                addColumn(projectId, "Doing"),
                addColumn(projectId, "Complete")
        );
        return defaultColumns;
    }

    /**
     * Adds a column to the database and returns an object with its values
     *
     * @param projectId the parent project id
     * @param name      the column name
     * @return a Column object or null
     */
    public static Column addColumn(int projectId, String name) {
        try (Connection conn = DriverManager.getConnection(url)) {

            Column column = new Column();
            column.setName(name);
            column.setTasks(new ArrayList<>());

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO columns (name, project_id) values (?,?); ");
            preparedStatement.setString(1, column.getName());
            preparedStatement.setInt(2, projectId);
            preparedStatement.execute();

            ResultSet resultSet = conn.prepareStatement(
                    "SELECT id FROM columns ORDER BY rowid DESC LIMIT 1;").executeQuery();
            resultSet.next();
            column.setId(resultSet.getInt(1));

            return column;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * adds a task to the database, retrieves an id from the database and sets it on the task object
     *
     * @param task the task to add to the database
     * @return the same task with the task id set
     */
    public static Task addTask(Task task) {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "insert into tasks (name, description, duedate, state, column_id) values (?,?,?,?,?);");
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setDate(3, new Date(task.getDueDate().getTimeInMillis()));
            preparedStatement.setString(4, task.getState().name());
            preparedStatement.setInt(5, task.getColumn().getId());
            preparedStatement.execute();

            ResultSet resultSet = conn.prepareStatement(
                    "SELECT id FROM tasks ORDER BY rowid DESC LIMIT 1;").executeQuery();
            resultSet.next();
            task.setId(resultSet.getInt(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return task;
    }


    /**
     * Gets default project id from database, if none found returns -1
     *
     * @param username
     * @return the default project id associated with the username
     */
    public static int getDefaultProject(String username) {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select default_project from workspaces where username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) : -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static List<Project> getUserProjects(Workspace workSpace) {
        List<Project> projects = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from projects where workspace_id = ?");
            preparedStatement.setInt(1, workSpace.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                projects.add(new Project(resultSet.getInt("id")
                        , resultSet.getString("name")
                        , workSpace));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return projects;
    }

    public static int getWorkSpaceId(String username) {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select id from workspaces where username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) : -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }

    public static List<Column> getProjectColumns(Project project) {
        List<Column> columns = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from columns where project_id = ?");
            preparedStatement.setInt(1, project.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }

    public static List<Task> getColumnTasks(Column column) {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from tasks where column_id = ?");
            preparedStatement.setInt(1, column.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Calendar duedate = Calendar.getInstance();
                duedate.setTimeInMillis(resultSet.getDate("date").getTime());
                tasks.add(new Task(
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        duedate,
                        TaskState.valueOf(resultSet.getString("state")),
                        column
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;

    }

    public static List<ListItem> getTaskListItems(Task task) {
        List<ListItem> items = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from list_items where task_id = ?");
            preparedStatement.setInt(1, task.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;

    }


    public static Task getTaskById(int i) {
        Task task = null;
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from tasks where id = ?");
            preparedStatement.setInt(1, i);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Calendar duedate = Calendar.getInstance();
                duedate.setTimeInMillis(resultSet.getDate("duedate").getTime());
                task = new Task(
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        duedate,
                        TaskState.valueOf(resultSet.getString("state")),
                        new Column(resultSet.getInt("column_id")) // TODO change
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return task;
    }

    public static User getUser(String text) {
        User user = null;
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from users where username = ?");
            preparedStatement.setString(1, text);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setUsermane(resultSet.getString("username"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setProfilePicturePath(resultSet.getString("profile_picture_path"));
            }
        } catch (SQLException | UserException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static Workspace loadWorkspace(User user) {
        Workspace workSpace = null;
        int defaultProject;
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from workspaces where username = ?");
            preparedStatement.setString(1, user.getUsermane());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                workSpace = new Workspace();
                workSpace.setUser(user);
                workSpace.setId(resultSet.getInt("id"));
                workSpace.setUsername(resultSet.getString("username"));
                workSpace.setProjects(loadProjects(workSpace));
                defaultProject = resultSet.getInt("default_project");
                workSpace.setDefaultProject(null);
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

    public static List<Project> loadProjects(Workspace workSpace) {
        List<Project> projects = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from projects where workspace_id = ?");
            preparedStatement.setInt(1, workSpace.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Project project = new Project();
                project.setWorkSpace(workSpace);
                project.setId(resultSet.getInt("id"));
                project.setName(resultSet.getString("name"));
                project.setColumns(loadColumns(project));

                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public static List<Column> loadColumns(Project project) {
        List<Column> columns = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from columns where project_id = ?");
            preparedStatement.setInt(1, project.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Column column = new Column();
                column.setProject(project);
                column.setId(resultSet.getInt("id"));
                column.setName(resultSet.getString("name"));
                column.setTasks(loadTasks(column));

                columns.add(column);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return columns;
    }

    public static List<Task> loadTasks(Column column) {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from tasks where column_id = ?");
            preparedStatement.setInt(1, column.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Task task = new Task();
                task.setColumn(column);
                task.setId(resultSet.getInt("id"));
                task.setName(resultSet.getString("name"));
                task.setDescription(resultSet.getString("description"));
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(resultSet.getDate("duedate").getTime());
                task.setDueDate(cal);
                task.setState(TaskState.valueOf(resultSet.getString("state")));
                task.setListItems(loadListItems(task));

                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static List<ListItem> loadListItems(Task task) {
        List<ListItem> items = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from list_items where task_id = ?");
            preparedStatement.setInt(1, task.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ListItem item = new ListItem();
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

    public static boolean deleteUser(User user) {
        return deleteRow(user);
    }

    private static <T> boolean deleteRow(T model) {
        // Check if operation can be performed
        if (!(model instanceof Identifiable))
            return false;

        // get class name and add 's' to match DB table names
        String tableName = model.getClass().getSimpleName() + "s";


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
                    "update tasks set name = ?, description = ?, duedate = ?, state = ?, column_id = ? where id = ? "
            );
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setDate(3, new Date(task.getDueDate().getTimeInMillis()));
            preparedStatement.setString(4, task.getState().name());
            preparedStatement.setInt(5, task.getColumn().getId());
            preparedStatement.setInt(6, task.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean updateColumn(Column column) {
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.prepareStatement("pragma foreign_keys = on;").execute();
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

    public static boolean updateProject(Project project) {
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.prepareStatement("pragma foreign_keys = on;").execute();
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
            conn.prepareStatement("pragma foreign_keys = on;").execute();

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
}