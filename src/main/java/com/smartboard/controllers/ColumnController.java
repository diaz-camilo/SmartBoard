package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.DBManager;
import com.smartboard.models.Column;
import com.smartboard.models.Task;
import com.smartboard.models.TaskState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class ColumnController {
    @FXML
    public VBox taskCardsContainer;
    public MenuItem deleteColumn;

    @FXML
    private VBox columnWrapper;

    @FXML
    private Label lblColumnName;

    @FXML
    private SplitMenuButton splitBtnAddTask;

    private Column column;

    @FXML
    void addChildren(ActionEvent event) throws IOException {

        // set up task object
        Task task = new Task();
        task.setName("testing task");
        task.setColumn(this.column);
        task.setListItems(new ArrayList<>());
        task.setDueDate(Calendar.getInstance());
        task.setState(TaskState.ACTIVE);
        task.setDescription("some description");

        // add task to the DB
        DBManager.addTask(task);

        // add task to column obj
        column.addTask(task);

        // generate task node and controller
        FXMLLoader taskLoader = new FXMLLoader(Application.class.getResource("task.fxml"));
        VBox vBoxTask = taskLoader.load();
        TaskController taskController = taskLoader.getController();
        taskController.init(task, vBoxTask);

        // add task to column UI
        addChildren(vBoxTask);
    }

    public void addChildren(VBox vBoxTask) {
        taskCardsContainer.getChildren().add(vBoxTask);
    }

    public void init(Column column) {
        this.column = column;
        this.column.setController(this);
        lblColumnName.setText(column.getName());
    }

    public void removeTask(TaskController taskController) {
        // remove from UI
        taskCardsContainer.getChildren().remove(taskController.getView());

        // remove from DB
        DBManager.deleteTask(taskController.getModel());
    }

    public void deleteColumn(ActionEvent event) {
    }
}