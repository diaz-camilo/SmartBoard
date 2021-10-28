package com.smartboard.controllers;

import com.smartboard.Utils.DBManager;
import com.smartboard.Utils.Utils;
import com.smartboard.models.User;
import com.smartboard.models.WorkSpace;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.function.Consumer;

public class MainApplicationController {

    public static User activeUser;
    private WorkSpace workSpace;

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

    @FXML
    void changeProfilePic(MouseEvent event) {

    }

    @FXML
    void editProfile(ActionEvent event) {

    }

    @FXML
    void logOut(ActionEvent event) {

    }


    @FXML
    public void initialize() {
        workSpace = DBManager.loadWorkspace(activeUser);
        txtQuote.setText(String.format("%s - %s", workSpace.getRandomQuote()));

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

}