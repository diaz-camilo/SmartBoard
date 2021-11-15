package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.DBManager;
import com.smartboard.Utils.Utils;
import com.smartboard.models.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
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
    public Text name;
    @FXML
    public ImageView imgClock;
    @FXML
    public Label dueDate;


    @FXML
    public Hyperlink linkDelete;
    @FXML
    public Hyperlink linkEdit;

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("d MMM yy");
    public TextArea description;
    public VBox taskCard;

    private Task model;
    private Node view;

    public Text getName() {
        return name;
    }

    public void setName(Text name) {
        this.name = name;
    }

    public void setNameText(String name) {
        this.name.setText(name);
    }

    public Label getDueDate() {
        return dueDate;
    }

    public void setDueDate(Label dueDate) {
        this.dueDate = dueDate;
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
        name.setText(model.getName());
        String duesDate = dateFormat.format(model.getDueDate().getTime());
        dueDate.setText(duesDate);
        description.setText(model.getDescription());
    }


    @FXML
    public void editTask(ActionEvent event) throws IOException {
        // generate edit task stage
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("editTask.fxml"));
        Stage editStage = new Stage();
        editStage.initModality(Modality.APPLICATION_MODAL);
        editStage.setTitle("Edit Task");
        editStage.setScene(new Scene(loader.load()));

        EditTaskController controller = loader.getController();
        controller.init(this);

        editStage.showAndWait();
    }

    public void saveChanges(String newName, String newDescription, String newDate) throws ParseException {

        // update model
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM. yy", Locale.ENGLISH);
        newCalendar.setTime(sdf.parse(newDate));
        model.update(newName, newDescription, newCalendar);
        // todo if model is valid, continue. else display error

        // update view

        setNameText(newName);
        setDescriptionText(newDescription);
        setDueDateText(newDate);

        // update DB
        DBManager.updateTask(model);
    }

    public void handleOnDragDetected(MouseEvent mouseEvent) {
        Utils.setDraggingObj(this);
        Dragboard db = this.taskCard.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("my task");
        db.setContent(content);
    }

    public void handleOnMouseDragged(MouseEvent mouseEvent) {
        mouseEvent.setDragDetect(true);
    }

    public void handleOnDragDropped(DragEvent event) {
        TaskController theTaskDropped = (TaskController) Utils.getDraggingObj();
        // the column I belong
        ColumnController myController = this.model.getColumn().getController();
        ColumnController theirController = theTaskDropped.getModel().getColumn().getController();


        if (myController == theirController && theTaskDropped != this) {
            //reorder tasks
            var childrenList = myController.taskCardsContainer.getChildren();
            childrenList.remove(theTaskDropped.view);
            int myPossition = childrenList.indexOf(this.view);
            childrenList.add(myPossition, theTaskDropped.view);
            event.consume();
        }
    }
}