package com.smartboard.controllers;

import com.smartboard.models.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    private Task model;
    private Node view;

    public Task getModel() {
        return model;
    }

    public void setModel(Task model) {
        this.model = model;
    }

    public Node getView() {
        return view;
    }

    public void setView(Node view) {
        this.view = view;
    }

    @FXML
    public void editTask(ActionEvent event) {
    }

    @FXML
    public void deleteTask(ActionEvent event) {
        model.getColumn().getController().removeTask(this);
    }


    public void init(Task model, Node view) {
        this.model = model;
        this.view = view;
        this.model.setController(this);

        try {
            imgClock.setImage(new Image(new FileInputStream("src/static/resources/img/task_clock.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        txtName.setText(model.getName());
        String duesDate = dateFormat.format(model.getDueDate().getTime());
        lblDueDate.setText(duesDate);
    }
}