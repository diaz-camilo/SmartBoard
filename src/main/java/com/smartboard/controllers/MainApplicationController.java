package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.DBManager;
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
import javafx.scene.input.MouseEvent;
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
    public Tab tabDefault;
    @FXML
    public Tab tabSecondProject;
    public MenuItem newProject;
    @FXML
    private Button btnLogOut;
    @FXML
    private Button btnProfile;
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

    @FXML
    void changeProfilePic(MouseEvent event) {
        System.out.println("mouse event");
    }

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
     * logs out current user
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void logOut(ActionEvent event) throws IOException {
        model = null;
        activeUser = null;

        // get main Stage - technique from Bro Code YouTube channel https://www.youtube.com/watch?v=wxhGKR3PQpo
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Generate a SignUp scene
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

        model = DBManager.loadWorkspace(activeUser);
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

        for (var project : model.getProjects()) {
            FXMLLoader tabLoader = new FXMLLoader(Application.class.getResource("project.fxml"));
            Tab tab = tabLoader.load();
            ProjectController projectController = tabLoader.getController();
            projectController.init(project, tab);
            for (var column : project.getColumns()) {
                FXMLLoader columnLoader = new FXMLLoader(Application.class.getResource("column.fxml"));
                VBox vBoxColumn = columnLoader.load();
                ColumnController columnController = columnLoader.getController();
                columnController.init(column, vBoxColumn);
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

        if (this.model.getDefaultProject() != null) {
            Tab defaultTab = this.model.getDefaultProject().getController().getView();
            defaultTab.setText("(*) " + defaultTab.getText());
            this.tabsProjects.getSelectionModel().select(defaultTab);
        }
    }

    public void removeProject(ProjectController projectController) {

        // remove project from DB
        DBManager.deleteProject(projectController.getModel());

        // remove project from model
        boolean result = this.tabsProjects.getTabs().remove(this.tabsProjects.getSelectionModel().getSelectedItem());

    }

    public void addNewProject(ActionEvent event) throws IOException {

        String dialogPrompt = "Enter new project name";
        String errorPrompt = "Project name can not be empty.\nPlease enter a name for the new project";
        String defaultVal = "New Project";
        String projectName = Utils.getStringFromDialog(dialogPrompt, errorPrompt, defaultVal);
        if (projectName == null)
            return;

        // create model
        Project project = DBManager.addProject(this.model.getId(), projectName);
        project.setWorkSpace(this.model);
        this.model.getProjects().add(project);

        // load view and controller
        FXMLLoader tabLoader = new FXMLLoader(Application.class.getResource("project.fxml"));
        Tab tab = tabLoader.load();
        ProjectController projectController = tabLoader.getController();
        projectController.init(project, tab);

        //add project to UI
        this.tabsProjects.getTabs().add(tab);
    }

    public void setDefaultProject(ProjectController defaultProjectController) {
        // Update model
        this.model.setDefaultProject(defaultProjectController.getModel());

        // Update DB
        DBManager.updateWorkspace(this.model);

        // update view
        for (Project project : this.model.getProjects()) {
            project.getController().setTabName(project.getName());
        }
        defaultProjectController.setTabName("(*) " + defaultProjectController.getModel().getName());
    }

    public void onCloseApp(ActionEvent event) {
        ((Stage) this.view.getScene().getWindow()).close();
    }

    public void onUnsetDefault(ActionEvent event) {
        // Update model
        this.model.setDefaultProject(null);

        // Update DB
        DBManager.updateWorkspace(this.model);

        // update view
        for (Project project : this.model.getProjects()) {
            project.getController().setTabName(project.getName());
        }
    }
}