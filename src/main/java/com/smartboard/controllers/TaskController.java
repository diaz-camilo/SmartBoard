package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.Utils;
import com.smartboard.models.interfaces.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TaskController {

    @FXML
    private Text name;
    @FXML
    private ImageView imgClock;
    @FXML
    private Label dueDate;
    @FXML
    private TextArea description;
    @FXML
    private VBox taskCard;

    private Task model;
    private Node view;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yy");

    public Text getName() {
        return name;
    }

    public void setName(Text name) {
        this.name = name;
    }

    public void setNameText(String name) {
        this.name.setText(name);
    }

    public void setDueDateText(String dateText) {
        this.dueDate.setText(dateText);
    }

    public TextArea getDescription() {
        return description;
    }

    public void setDescription(TextArea description) {
        this.description = description;
    }

    public void setDescriptionText(String description) {
        this.description.setText(description);
    }

    public Task getModel() {
        return model;
    }

    public Node getView() {
        return view;
    }

    /**
     * initiates the controller and set the model in the controller and vice versa
     *
     * @param model the task model
     */
    public void init(Task model) {
        // set model and view on this controller
        this.model = model;
        this.view = this.taskCard;

        // set controller on model
        this.model.setController(this);

        // set view to reflect model
        try {
            imgClock.setImage(new Image(new FileInputStream("src/static/resources/img/task_clock.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String dueDate = dateFormat.format(model.getDueDate().getTime());
        name.setText(model.getName());
        this.dueDate.setText(dueDate);
        description.setText(model.getDescription());
    }

    /**
     * delete task from model and view
     *
     * @param event
     */
    @FXML
    public void onDeleteLinkClick(ActionEvent event) {
        // remove from column
        model.getColumn().getController().removeTask(this.view);
        // remove from DB
        model.delete();
    }

    /**
     * invokes a helper stage to edit the task details.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void onEditLinkClick(ActionEvent event) throws IOException {
        // generate edit window stage
        Stage editStage = new Stage();
        editStage.initModality(Modality.APPLICATION_MODAL);
        editStage.setTitle("Edit Task");

        // generate edit task and initiate its controller with this controller,
        // so it can talk back
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("editableTask.fxml"));
        Parent editTask = loader.load();
        EditableTaskController controller = loader.getController();
        controller.init(this);
        controller.setTitle("Edit Task");

        // set scene and show
        editStage.setScene(new Scene(editTask));
        editStage.showAndWait();
    }

    /**
     * updates view with the given arguments and also updates the model
     *
     * @param newName
     * @param newDescription
     * @param newDate
     * @throws ParseException
     */
    public void saveChanges(String newName, String newDescription, String newDate) throws ParseException {
        // update model
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM. yy", Locale.ENGLISH);
        newCalendar.setTime(sdf.parse(newDate));
        model.update(newName, newDescription, newCalendar);

        // update view
        setNameText(newName);
        setDescriptionText(newDescription);
        setDueDateText(newDate);
    }

    /**
     * sets up the application for dragging
     *
     * @param mouseEvent
     */
    public void onDragDetected(MouseEvent mouseEvent) {
        Utils.setDraggingObj(this);
        Dragboard db = this.taskCard.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("my task");
        db.setContent(content);
    }

    public void onMouseDragged(MouseEvent mouseEvent) {
        mouseEvent.setDragDetect(true);
    }

    /**
     * Handles when a task is dropped on another task for reordering.
     * The task being dropped will be placed on top of the task below
     *
     * @param event
     */
    public void onDragDropped(DragEvent event) {
        // retrieve task from temp holding storage
        TaskController theTaskDropped = (TaskController) Utils.getDraggingObj();

        // the column I belong
        ColumnController myController = this.model.getColumn().getController();
        ColumnController theirController = theTaskDropped.getModel().getColumn().getController();


        if (myController == theirController && theTaskDropped != this) {
            //reorder tasks
            var childrenList = myController.getTaskCardsContainer().getChildren();
            childrenList.remove(theTaskDropped.view);
            int myPosition = childrenList.indexOf(this.view);
            // in view
            childrenList.add(myPosition, theTaskDropped.view);
            // in model
            this.model.getColumn().shiftTasks(myPosition, theTaskDropped.getModel());
        }
    }
}