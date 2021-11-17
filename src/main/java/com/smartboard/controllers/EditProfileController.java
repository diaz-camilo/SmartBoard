package com.smartboard.controllers;

import com.smartboard.Utils.DBManager;
import com.smartboard.Utils.Utils;
import com.smartboard.exceptions.UserException;
import com.smartboard.models.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    public ImageView profilePic;

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
    private MainApplicationController mainAppController;

    public void initialize() {
        try {
            profilePic.setImage(new Image(new FileInputStream("src/static/resources/img/default_profile_pic.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void saveChanges(Event event) throws IOException {
        if (!password.getText().equals(passwordConfirm.getText())) {
            lblPasswordError.setText("Password does not match");
            lblPasswordError.setPrefHeight(Label.USE_COMPUTED_SIZE);
            lblPasswordError.setMinHeight(Label.USE_COMPUTED_SIZE);
            lblPasswordError.setVisible(true);
        } else {
            try {
                // get active user
                User user = MainApplicationController.activeUser;

                // update DB
                if (password.getText().strip().isBlank()) {
                    DBManager.updateUser(username.getText(),
                            firstname.getText(), lastname.getText(), profilePicPath);
                } else {
                    DBManager.updateUser(username.getText(),
                            password.getText(), firstname.getText(), lastname.getText(), profilePicPath);
                    user.getLogin().setPassword(password.getText());
                }

                // update model
                user.setFirstName(firstname.getText());
                user.setLastName(lastname.getText());
                user.setProfilePicturePath(profilePicPath);

                // update main app ui
                this.mainAppController.username.setText(username.getText());
                this.mainAppController.imgProfilePic.setImage(this.profilePic.getImage());
            } catch (UserException e) {
                lblSignUpError.setText(e.getMessage());
            }
            close(event);
        }
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