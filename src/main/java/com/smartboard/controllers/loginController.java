package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class loginController {

    @FXML
    private Button btnClose;

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink linkSingUp;

    @FXML
    private Label lblError;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    public void close(ActionEvent event) {
        // get main Stage - technique from Bro Code YouTube channel https://www.youtube.com/watch?v=wxhGKR3PQpo
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }


    // TODO "For testing only" erase before submitting
    @FXML
    public void initialize() throws IOException {
        // set user and password to speed testing
        username.setText("camilo");
        password.setText("123");
    }

    @FXML
    public void login(ActionEvent event) throws IOException {
        if (DBManager.AuthenticateUser(username.getText(), password.getText())) {
            MainApplicationController.activeUser = DBManager.getUser(username.getText());
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
        } else {
            this.lblError.setText("Please check your Username or Password and try again.");
        }
    }

    @FXML
    public void goToSignUp(ActionEvent event) throws IOException {
        // get main Stage - technique from Bro Code YouTube channel https://www.youtube.com/watch?v=wxhGKR3PQpo
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Generate a SignUp scene
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("signUp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);
        stage.show();
    }
}