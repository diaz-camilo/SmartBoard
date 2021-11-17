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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    public void addTask(ActionEvent event) throws IOException {


        // set up new task object
        Task task = new Task();
        task.setName("Task name");
        task.setIndex(0);
        task.setColumn(this.model);
        task.setListItems(new ArrayList<>());
        task.setDueDate(Calendar.getInstance());
        task.setState(TaskState.ACTIVE);
        task.setDescription("Write description here");

        // generate task node and controller
        FXMLLoader taskLoader = new FXMLLoader(Application.class.getResource("task.fxml"));
        VBox vBoxTask = taskLoader.load();
        TaskController taskController = taskLoader.getController();
        taskController.init(task);

        // generate new task stage
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("newTask.fxml"));
        Stage newTaskStage = new Stage();
        newTaskStage.initModality(Modality.APPLICATION_MODAL);
        newTaskStage.setTitle("New Task");
        newTaskStage.setScene(new Scene(loader.load()));
        NewTaskController newTaskController = loader.getController();
        newTaskController.init(taskController);

        newTaskStage.showAndWait();

        if (newTaskController.isSaveTask()) {

            // add task to the DB
            DBManager.addTask(task);

            // add task to column obj
            model.shiftTasks(0, task);

            // add task to column UI
            addTaskOnTop(vBoxTask);
        }
    }

    public void addTaskOnTop(Node taskNode) {
        taskCardsContainer.getChildren().add(0, taskNode);
    }

    public void addTask(Node taskNode) {
        taskCardsContainer.getChildren().add(taskNode);
    }

    public void init(Column model, Node view) {
        this.model = model;
        this.view = view;
        this.model.setController(this);

        lblColumnName.setText(model.getName());
    }

    public void removeTask(Node task) {
        taskCardsContainer.getChildren().remove(task);
    }

    public void deleteColumn(ActionEvent event) {
        model.getProject().getController().removeColumn(this);
    }

    public void handleOnDragDropped(DragEvent event) {

        //retrieve task controller
        TaskController taskController = (TaskController) Utils.getDraggingObj();
        Task task = taskController.getModel();

        if (!this.taskCardsContainer.getChildren().contains(taskController.getView())) {

            // update model:
            // * remove from previous column
            task.getColumn().removeTask(taskController.getModel());
            // * add to new column on top
            task.setColumn(this.model);
            this.model.shiftTasks(0, taskController.getModel());


//            //update DB
//            DBManager.updateTask(taskController.getModel());

            // update UI
            this.taskCardsContainer.getChildren().add(0, taskController.getView());
            event.consume();
        }
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