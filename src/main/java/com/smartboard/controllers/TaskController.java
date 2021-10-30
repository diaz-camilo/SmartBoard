package com.smartboard.controllers;

import com.smartboard.models.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class TaskController {
    @FXML
    public Text txtName;
    @FXML
    public ImageView imgClock;
    @FXML
    public Label lblDueDate;
    @FXML
    public Hyperlink linkDelete;
    @FXML
    public Hyperlink linkEdit;

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd MMM h:mm aa");


    @FXML
    public void editTask(ActionEvent event) {
    }

    @FXML
    public void DeleteTask(ActionEvent event) {
    }

    public void init(Task task) {

        try {
            imgClock.setImage(new Image(new FileInputStream("src/static/resources/img/task_clock.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        txtName.setText(task.getName());
        String duesDate = dateFormat.format(task.getDueDate().getTime());
        lblDueDate.setText(duesDate);
    }
}