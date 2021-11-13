package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.DBManager;
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
    @FXML
    private Button btnLogOut;
    @FXML
    private Button btnProfile;
    @FXML
    private ImageView imgProfilePic;
    @FXML
    private Label lblUsername;
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
    void editProfile(ActionEvent event) {

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
        lblUsername.setText(model.getUsername());
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
            projectController.init(project);
            for (var column : project.getColumns()) {
                FXMLLoader columnLoader = new FXMLLoader(Application.class.getResource("column.fxml"));
                VBox vBoxColumn = columnLoader.load();
                ColumnController columnController = columnLoader.getController();
                columnController.init(column, vBoxColumn);
                for (var task : column.getTasks()) {
                    FXMLLoader taskLoader = new FXMLLoader(Application.class.getResource("task.fxml"));
                    VBox vBoxTask = taskLoader.load();
                    TaskController taskController = taskLoader.getController();
                    taskController.init(task, vBoxTask);
                    columnController.addChildren(vBoxTask); // add each task
                }// tasks for loop
                projectController.addColumn(vBoxColumn); // add each column
            } // columns for loop
            this.tabsProjects.getTabs().add(tab); // add each project
        } // projects for loop

    }

    @FXML
    void ProjectAdd(ActionEvent event) {

    }

    @FXML
    void ProjectDelete(ActionEvent event) {

    }

    @FXML
    void ProjectRemove(ActionEvent event) {

    }

    @FXML
    void ProjectUpdate(ActionEvent event) {

    }

    public void addTask(ActionEvent event) {

    }

    public void removeProject(ProjectController projectController) {
        System.out.println("MAController::removePriject");

        // remove project from DB
        DBManager.deleteProject(projectController.getModel());

        // remove project from model
        boolean result = this.tabsProjects.getTabs().remove(this.tabsProjects.getSelectionModel().getSelectedItem());

        System.out.println(result);
    }
}