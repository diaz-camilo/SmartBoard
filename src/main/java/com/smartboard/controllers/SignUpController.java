package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.DBManager;
import com.smartboard.exceptions.UserException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {

    @FXML
    private Button btnClose;

    @FXML
    private Button btnSignUp;

    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private Label lblPasswordError;

    @FXML
    private Label lblSignUpError;

    @FXML
    private Hyperlink linkSingUp;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField passwordConfirm;

    @FXML
    private TextField username;

    @FXML
    void SignUp(ActionEvent event) throws IOException {
        if (!password.getText().equals(passwordConfirm.getText())) {
            lblPasswordError.setText("Password does not match");
            lblPasswordError.setPrefHeight(Label.USE_COMPUTED_SIZE);
            lblPasswordError.setMinHeight(Label.USE_COMPUTED_SIZE);
            lblPasswordError.setVisible(true);
        } else {
            try {
                DBManager.RegisterUser(username.getText(),
                        password.getText(), firstname.getText(), lastname.getText());
                MainApplicationController.activeUser = DBManager.getUser(username.getText());
                // TODO Generate workspace with template project
                // get main Stage - technique from Bro Code YouTube channel https://www.youtube.com/watch?v=wxhGKR3PQpo
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // Generate a SignUp scene
                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainApplication.fxml"));
                Node view = fxmlLoader.load();
                MainApplicationController controller = fxmlLoader.getController();
                controller.setView(view);
                Scene scene = new Scene((Parent) view);
                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();
            } catch (UserException e) {
                lblSignUpError.setText(e.getMessage());
            }
        }
    }

    @FXML
    void close(ActionEvent event) {
        // get main Stage - technique from Bro Code YouTube channel https://www.youtube.com/watch?v=wxhGKR3PQpo
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void goToSignUp(ActionEvent event) throws IOException {
        // get main Stage - technique from Bro Code YouTube channel https://www.youtube.com/watch?v=wxhGKR3PQpo
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Generate a SignUp scene
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

}