package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.Utils;
import com.smartboard.models.Project;
import com.smartboard.models.User;
import com.smartboard.models.Workspace;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainApplicationController {

    public static User activeUser;

    @FXML
    public TabPane tabsProjects;
    @FXML
    public MenuItem newProject;
    @FXML
    public ImageView imgProfilePic;
    @FXML
    public Label username;
    @FXML
    private Text txtQuote;

    private Workspace model;
    private Node view;

    public void setView(Node view) {
        this.view = view;
    }

    /**
     * generates an edit profile form for the user,
     * Then, the user will have the option to change profile picture, name, last name and
     * password
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void editProfile(ActionEvent event) throws IOException {

        // generate edit task stage
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("editProfile.fxml"));
        Stage editStage = new Stage();
        editStage.initModality(Modality.APPLICATION_MODAL);
        editStage.setTitle("Edit Profile");
        editStage.setScene(new Scene(loader.load()));

        EditProfileController controller = loader.getController();
        controller.init(this);

        editStage.showAndWait();
    }

    /**
     * logs out current user and display login window
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void logOut(ActionEvent event) throws IOException {
        // nullify model and user
        model = null;
        activeUser = null;

        // display login window
        Stage stage = Utils.getStageFromEvent(event);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets up main application by loading active user workspace and loading view
     *
     * @throws IOException
     */
    @FXML
    public void initialize() throws IOException {

        model = Workspace.getUserWorkspace(activeUser);
        model.setController(this);
        activeUser.setWorkSpace(model);

        txtQuote.setText(String.format("%s - %s", model.getRandomQuote()));
        username.setText(model.getUsername());
        try {
            InputStream inputStream = new FileInputStream(
                    activeUser.getProfilePicturePath() != null ?
                            activeUser.getProfilePicturePath() :
                            "src/static/resources/img/default_profile_pic.png");

            imgProfilePic.setImage(new Image(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // populate UI and initialize each controller with its respective model and vice versa
        for (var project : model.getProjects()) {
            FXMLLoader tabLoader = new FXMLLoader(Application.class.getResource("project.fxml"));
            Tab tab = tabLoader.load();
            ProjectController projectController = tabLoader.getController();
            projectController.init(project);
            for (var column : project.getColumns()) {
                FXMLLoader columnLoader = new FXMLLoader(Application.class.getResource("column.fxml"));
                VBox vBoxColumn = columnLoader.load();
                ColumnController columnController = columnLoader.getController();
                columnController.init(column);
                for (var task : column.getTasks()) {
                    FXMLLoader taskLoader = new FXMLLoader(Application.class.getResource("task.fxml"));
                    VBox vBoxTask = taskLoader.load();
                    TaskController taskController = taskLoader.getController();
                    taskController.init(task);
                    columnController.addTask(vBoxTask); // add each task
                }// tasks for loop
                projectController.addColumn(vBoxColumn); // add each column
            } // columns for loop
            this.tabsProjects.getTabs().add(tab); // add each project
        } // projects for loop

        // if user has a default project, select it
        if (this.model.getDefaultProject() != null) {
            Tab defaultTab = this.model.getDefaultProject().getController().getView();
            defaultTab.setText("(*) " + defaultTab.getText());
            this.tabsProjects.getSelectionModel().select(defaultTab);
        }
    }

    /**
     * deletes the currently selected project
     *
     * @param projectController
     */
    public void removeProject(ProjectController projectController) {
        // update model
        Project project = projectController.getModel();
        model.getProjects().remove(project);
        project.delete();

        // update UI
        this.tabsProjects.getTabs().remove(this.tabsProjects.getSelectionModel().getSelectedItem());
    }

    /**
     * Adds an empty project to the workspace
     *
     * @param event
     * @throws IOException
     */
    public void addNewProject(ActionEvent event) throws IOException {

        String dialogPrompt = "Enter new project name";
        String errorPrompt = "Project name can not be empty.\nPlease enter a name for the new project";
        String defaultVal = "New Project";
        String projectName = Utils.getStringFromDialog(dialogPrompt, errorPrompt, defaultVal);
        if (projectName == null)
            return;

        // create model
        Project project = new Project(projectName, this.model);
        project.setWorkSpace(this.model);
        this.model.getProjects().add(project);

        // load view and controller
        FXMLLoader tabLoader = new FXMLLoader(Application.class.getResource("project.fxml"));
        Tab tab = tabLoader.load();
        ProjectController projectController = tabLoader.getController();
        projectController.init(project);

        //add project to UI
        this.tabsProjects.getTabs().add(tab);
    }

    /**
     * sets project as default, resets the others
     *
     * @param defaultProjectController
     */
    public void setDefaultProject(ProjectController defaultProjectController) {
        // Update model
        this.model.setDefault(defaultProjectController.getModel());

        // update view
        for (Project project : this.model.getProjects()) {
            project.getController().setTabName(project.getName());
        }
    }

    /**
     * resets all projects, sets null as default
     *
     * @param event
     */
    public void onUnsetDefault(ActionEvent event) {
        // Update model
        this.model.setDefault(null);

        // update view
        for (Project project : this.model.getProjects()) {
            project.getController().setTabName(project.getName());
        }
    }

    public void onCloseApp(ActionEvent event) {
        Utils.getStageFromEvent(event).close();
    }
}