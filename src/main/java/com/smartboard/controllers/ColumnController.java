package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.DBManager;
import com.smartboard.Utils.Utils;
import com.smartboard.models.Column;
import com.smartboard.models.Task;
import com.smartboard.models.TaskState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class ColumnController {
    @FXML
    public VBox taskCardsContainer;

    @FXML
    public MenuItem deleteColumn;

    @FXML
    private VBox columnWrapper;

    @FXML
    private Label lblColumnName;

    @FXML
    private SplitMenuButton splitBtnAddTask;

    private Column model;
    private Node view;

    public Column getModel() {
        return model;
    }

    public void setModel(Column model) {
        this.model = model;
    }

    public Node getView() {
        return view;
    }

    public void setView(Node view) {
        this.view = view;
    }

    /**
     * Add a new task to the column
     *
     * @param event add task button event
     */
    @FXML
    public void addChildren(ActionEvent event) throws IOException {

        // set up task object
        Task task = new Task();
        task.setName("testing task");
        task.setColumn(this.model);
        task.setListItems(new ArrayList<>());
        task.setDueDate(Calendar.getInstance());
        task.setState(TaskState.ACTIVE);
        task.setDescription("some description");
        this.model.getTasks().add(task);

        // add task to the DB
        DBManager.addTask(task);

        // add task to column obj
        model.addTask(task);

        // generate task node and controller
        FXMLLoader taskLoader = new FXMLLoader(Application.class.getResource("task.fxml"));
        VBox vBoxTask = taskLoader.load();
        TaskController taskController = taskLoader.getController();
        taskController.init(task, vBoxTask);

        // add task to column UI
        addChildren(vBoxTask);
    }

    public void addChildren(Node taskNode) {
        taskCardsContainer.getChildren().add(taskNode);
    }

    public void init(Column model, Node view) {
        this.model = model;
        this.view = view;
        this.model.setController(this);

        lblColumnName.setText(model.getName());
    }

    public void removeTask(TaskController taskController) {
        // remove from UI
        taskCardsContainer.getChildren().remove(taskController.getView());

        // remove from DB
        DBManager.deleteTask(taskController.getModel());
    }

    public void deleteColumn(ActionEvent event) {
        model.getProject().getController().removeColumn(this);
    }

    public void handleOnDragDropped(DragEvent event) {

        //retrieve task controller
        TaskController taskController = (TaskController) Utils.getDraggingObj();

        if (this.taskCardsContainer.getChildren().contains(taskController.getView())) {
            // todo reorder tasks
        } else {

            taskController.getModel().setColumn(this.model);

            //update DB
            DBManager.updateTask(taskController.getModel());

            // update UI
            this.taskCardsContainer.getChildren().add(taskController.getView());
        }
        event.consume();
    }

    public void handleOnDragOver(DragEvent event) {
        if (event.getGestureSource() != this.view && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    public void editColumnName(ActionEvent event) {
        String dialogPrompt = "Enter new column name";
        String errorPrompt = "Column name can not be empty.\nPlease enter a name for the column";
        String defaultVal = model.getName();
        String columnName = Utils.getStringFromDialog(dialogPrompt, errorPrompt, defaultVal);
        if (columnName == null)
            return;

        // update model
        model.setName(columnName);

        // update DB
        DBManager.updateColumn(model);

        // update UI
        this.lblColumnName.setText(columnName);
    }
}