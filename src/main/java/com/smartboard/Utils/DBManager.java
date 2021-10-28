package com.smartboard.Utils;

import com.smartboard.exceptions.UserException;
import com.smartboard.models.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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

    public static boolean RegisterUser(String username, String password, String firstname, String lastname) throws UserException {
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

            PreparedStatement userStatement = conn.prepareStatement(
                    "insert into users (username, firstname, lastname) values (?,?,?);");
            userStatement.setString(1, username);
            userStatement.setString(2, firstname);
            userStatement.setString(3, lastname);

            PreparedStatement loginStatement = conn.prepareStatement(
                    "insert into logins (username, password_hash) VALUES (?,?)");
            loginStatement.setString(1, username);
            loginStatement.setString(2, pwdHash);

            userStatement.execute();
            loginStatement.execute();

            return true;

        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("unique constraint failed: users.username"))
                throw new UserException("> Username already taken, please try a different Username");
        }
        return false;
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

    public static List<Project> getUserProjects(WorkSpace workSpace) {
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

    public static void addTask(Task task) {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "insert into tasks (name, description, duedate, state, column_id) values (?,?,?,?,?);");
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setDate(3, new Date(task.getDueDate().getTimeInMillis()));
            preparedStatement.setString(4, task.getState().name());
            preparedStatement.setInt(5, task.getColumn().getId());
            System.out.println(preparedStatement.execute());
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public static WorkSpace loadWorkspace(User user) {
        WorkSpace workSpace = null;
        int defaultProject;
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select * from workspaces where username = ?");
            preparedStatement.setString(1, user.getUsermane());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                workSpace = new WorkSpace();
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

    public static List<Project> loadProjects(WorkSpace workSpace) {
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
}