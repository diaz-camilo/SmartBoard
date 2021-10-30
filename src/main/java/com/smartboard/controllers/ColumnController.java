package com.smartboard.controllers;

import com.smartboard.models.Column;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.VBox;

public class ColumnController {
    @FXML
    public VBox taskCardsContainer;
    @FXML
    private VBox columnWrapper;

    @FXML
    private Label lblColumnName;

    @FXML
    private SplitMenuButton splitBtnAddTask;

    @FXML
    void addTask(ActionEvent event) {

    }

    public void addTask(VBox vBoxTask) {
        taskCardsContainer.getChildren().add(vBoxTask);
    }

    public void init(Column column) {
        lblColumnName.setText(column.getName());
    }
}