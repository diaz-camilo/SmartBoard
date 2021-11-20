package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.Utils;
import com.smartboard.models.interfaces.Column;
import com.smartboard.models.interfaces.Task;
import com.smartboard.models.TaskImpl;
import com.smartboard.models.TaskState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Calendar;

public class ColumnController {
    @FXML
    private VBox taskCardsContainer;
    @FXML
    private MenuItem deleteColumn;
    @FXML
    private VBox columnWrapper;
    @FXML
    private Label lblColumnName;

    private Column model;
    private Node view;

    public Column getModel() {
        return model;
    }

    public Node getView() {
        return view;
    }

    public VBox getTaskCardsContainer() {
        return this.taskCardsContainer;
    }

    /**
     * Add a new task to the column
     *
     * @param event add task button event
     */
    @FXML
    public void addTask(ActionEvent event) throws IOException {

        // set up new task object
        Task task = new TaskImpl("Task Name", "Write Description Here",
                Calendar.getInstance(), TaskState.ACTIVE, this.model, 0);

        // generate task node and controller
        FXMLLoader taskLoader = new FXMLLoader(Application.class.getResource("task.fxml"));
        VBox vBoxTask = taskLoader.load();
        TaskController taskController = taskLoader.getController();
        taskController.init(task);

        // generate new task stage
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("editableTask.fxml"));
        Stage newTaskStage = new Stage();
        newTaskStage.initModality(Modality.APPLICATION_MODAL);
        newTaskStage.setTitle("New Task");
        newTaskStage.setScene(new Scene(loader.load()));
        EditableTaskController editableTaskController = loader.getController();
        editableTaskController.init(taskController);
        editableTaskController.setTitle("New Task");


        newTaskStage.showAndWait();

        if (editableTaskController.isSaveTask()) {

            // add task to column obj
            model.shiftTasks(0, task);

            // add task to column UI
            addTaskOnTop(vBoxTask);
        } else {
            task.delete();
        }
    }

    /**
     * places a task at the top of the column
     *
     * @param taskNode
     */
    public void addTaskOnTop(Node taskNode) {
        taskCardsContainer.getChildren().add(0, taskNode);
    }

    /**
     * adds a task at the end of the column
     *
     * @param taskNode
     */
    public void addTask(Node taskNode) {
        taskCardsContainer.getChildren().add(taskNode);
    }

    /**
     * initializes the controller to reflect the model values
     *
     * @param model
     */
    public void init(Column model) {
        this.model = model;
        this.view = this.columnWrapper;
        this.model.setController(this);

        lblColumnName.setText(model.getName());
    }

    /**
     * removes a task card from the GUI
     *
     * @param task
     */
    public void removeTask(Node task) {
        taskCardsContainer.getChildren().remove(task);
    }

    /**
     * sends a request to the parent project controller to delete this column and all its contents
     *
     * @param event
     */
    public void deleteColumn(ActionEvent event) {
        model.getProject().getController().removeColumn(this);
    }

    /**
     * handles when a task card from another column is dropped on this column and places it at the top of the column
     *
     * @param event
     */
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

    /**
     * enables drop over
     *
     * @param event
     */
    public void handleOnDragOver(DragEvent event) {
        if (event.getGestureSource() != this.view && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    /**
     * generates a text input dialog to change the columns name
     *
     * @param event
     */
    public void editColumnName(ActionEvent event) {
        String dialogPrompt = "Enter new column name";
        String errorPrompt = "Column name can not be empty.\nPlease enter a name for the column";
        String defaultVal = model.getName();
        String columnName = Utils.getStringFromDialog(dialogPrompt, errorPrompt, defaultVal);
        if (columnName == null)
            return;

        // update model
        model.setName(columnName);

        // update UI
        this.lblColumnName.setText(columnName);
    }
}