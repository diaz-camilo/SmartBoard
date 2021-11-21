package com.smartboard.controllers;

import com.smartboard.Utils.Utils;
import com.smartboard.exceptions.UserException;
import com.smartboard.models.interfaces.User;
import javafx.event.Event;
import javafx.fxml.FXML;
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

public class EditProfileController {

    @FXML
    private Label usernameError;
    @FXML
    private Label firstnameError;
    @FXML
    private Label lastnameError;
    @FXML
    private ImageView profilePic;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private Label passwordError;
    @FXML
    private Label lblSignUpError;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField passwordConfirm;
    @FXML
    private TextField username;
    private String profilePicPath = null;
    private MainApplicationController mainAppController;

    /**
     * preload default profile pic
     */
    public void initialize() {
        try {
            profilePic.setImage(new Image(new FileInputStream("src/static/resources/img/default_profile_pic.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * validates password and then attempts to update model and GUI
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void saveChanges(Event event) {
        hideError(firstnameError);
        hideError(lastnameError);
        hideError(passwordError);

        boolean isValid = true;
        if (Utils.NullOrEmpty(this.firstname.getText())) {
            showError(firstnameError);
            isValid = false;
        }
        if (Utils.NullOrEmpty(this.lastname.getText())) {
            showError(lastnameError);
            isValid = false;
        }
        if (!password.getText().equals(passwordConfirm.getText())) {
            showError(passwordError);
            isValid = false;
        }
        if (isValid) {
            try {
                // get active user
                User user = MainApplicationController.activeUser;

                // update model
                user.updateDetails(firstname.getText(), lastname.getText(), profilePicPath);

                if (!password.getText().strip().isBlank())
                    user.getLogin().updatePassword(password.getText());

                // update main app ui
                this.mainAppController.changeProfilePic(this.profilePic.getImage());
                close(event);
            } catch (UserException e) {
                lblSignUpError.setText(e.getMessage());
            }
        }
    }

    private void showError(Label label) {
        label.setPrefHeight(Label.USE_COMPUTED_SIZE);
        label.setMinHeight(Label.USE_COMPUTED_SIZE);
        label.setVisible(true);
    }

    private void hideError(Label label) {
        label.setPrefHeight(0);
        label.setMinHeight(0);
        label.setVisible(false);
    }

    @FXML
    void close(Event event) {
        Utils.getStageFromEvent(event).close();
    }


    public void handleOnImageClick(MouseEvent mouseEvent) {

        Stage stage = Utils.getStageFromEvent(mouseEvent);

        FileChooser fileChooser = new FileChooser();
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

    public void init(MainApplicationController mainApplicationController) {

        this.mainAppController = mainApplicationController;
        User user = MainApplicationController.activeUser;
        // load and set user data into view
        this.username.setText(user.getUsername());
        this.firstname.setText(user.getFirstName());
        this.lastname.setText(user.getLastName());
        String profilePicPath = user.getProfilePicturePath();

        if (profilePicPath != null) {
            try {
                this.profilePic.setImage(new Image(new FileInputStream(profilePicPath)));
                this.profilePicPath = profilePicPath;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("unable to load profile pic EditProfileController::init");
            }
        }
    }

    public void onEnter(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER)
            saveChanges(keyEvent);
    }
}