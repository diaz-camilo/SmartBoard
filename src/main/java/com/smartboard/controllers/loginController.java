package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.Utils;
import com.smartboard.models.Login;
import com.smartboard.models.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class loginController {

    @FXML
    private Label lblError;
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;

    @FXML
    public void close(ActionEvent event) {
        Utils.getStageFromEvent(event).close();
    }


//    // TODO "For testing only" erase before submitting
//    @FXML
//    public void initialize() throws IOException {
//        // set user and password to speed testing
//        username.setText("camilo");
//        password.setText("123");
//    }

    /**
     * verify credentials and loads application if valid,
     * display error message if incorrect
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void login(Event event) throws IOException {
        if (Login.authenticate(username.getText(), password.getText())) {
            MainApplicationController.activeUser = User.build(username.getText());
            Stage stage = Utils.getStageFromEvent(event);

            // load App GUI
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

    /**
     * redirect to signup window
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void goToSignUp(ActionEvent event) throws IOException {
        Stage stage = Utils.getStageFromEvent(event);
        // Generate a SignUp scene
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("signUp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);
        stage.show();
    }

    /**
     * attempts to login on enter pressed
     *
     * @param keyEvent
     * @throws IOException
     */
    public void onEnter(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER)
            login(keyEvent);
    }
}