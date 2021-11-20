package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.Utils;
import com.smartboard.exceptions.UserException;
import com.smartboard.models.UserImpl;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class SignUpController {

    private ImageView profilePic;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private Label lblPasswordError;
    @FXML
    private Label lblSignUpError;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField passwordConfirm;
    @FXML
    private TextField username;

    private String profilePicPath = null;

    /**
     * Initialize controller and load default pic from file system
     */
    public void initialize() {
        try {
            profilePic.setImage(new Image(new FileInputStream("src/static/resources/img/default_profile_pic.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * attempts to sign up user and sets error messages if something goes wrong or
     * redirects to login page if there were no issues with user creation
     *
     * @param event
     */
    @FXML
    void signUp(Event event) throws IOException {
        // clear errors
        lblSignUpError.setText("");
        lblPasswordError.setText("");

        // validate password and confirm password match and display error if they don't.
        // if they match, continue with the sign-up process
        if (!password.getText().equals(passwordConfirm.getText())) {
            lblPasswordError.setText("Password does not match");
            lblPasswordError.setPrefHeight(Label.USE_COMPUTED_SIZE);
            lblPasswordError.setMinHeight(Label.USE_COMPUTED_SIZE);
            lblPasswordError.setVisible(true);
        } else {
            // Register new user and redirect to login screen
            try {
                new UserImpl(firstname.getText(),
                        lastname.getText(), username.getText(), password.getText(), profilePicPath);
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Account Created");
                success.setContentText("Your account was created successfully, you will be redirected to the login" +
                        "window.");
                success.showAndWait();
                goToLogin(event);
            } catch (UserException e) {
                lblSignUpError.setText(e.getMessage());
            }
        }
    }

    /**
     * Close the app
     *
     * @param event
     */
    @FXML
    void close(ActionEvent event) {
        Utils.getStageFromEvent(event).close();
    }

    /**
     * Loads login view
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void goToLogin(Event event) throws IOException {
        // get main Stage - technique from Bro Code YouTube channel https://www.youtube.com/watch?v=wxhGKR3PQpo
        Stage stage = Utils.getStageFromEvent(event);
        // Generate a SignUp scene
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Displays a file picker dialog for the user to select a profile picture
     *
     * @param mouseEvent
     */
    public void handleOnImageClick(MouseEvent mouseEvent) {

        Stage stage = Utils.getStageFromEvent(mouseEvent);

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("BMP Files", "*.bmp"),
                new FileChooser.ExtensionFilter("GIF Files", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                profilePic.setImage(new Image(new FileInputStream(selectedFile.getAbsolutePath())));
                profilePicPath = selectedFile.getAbsolutePath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void onEnter(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER)
            signUp(keyEvent);
    }
}