package com.smartboard.controllers;

import com.smartboard.Utils.Utils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class EditableTaskController {
    @FXML
    private Text title;
    @FXML
    private Label nameError;
    @FXML
    private Label descriptionError;
    @FXML
    private DatePicker date;
    @FXML
    private TextArea description;
    @FXML
    private TextField name;

    private TaskController taskController;

    private boolean saveTask = false;

    public boolean isSaveTask() {
        return saveTask;
    }

    /**
     * initiate controller and UI to values on task controller
     *
     * @param taskController the initiating state
     */
    public void init(TaskController taskController) {
        this.taskController = taskController;
        Calendar modelCalendar = taskController.getModel().getDueDate();
        date.setValue(
                LocalDateTime.ofInstant(
                        modelCalendar.toInstant(),
                        modelCalendar.getTimeZone().toZoneId()
                ).toLocalDate()
        );
        name.setText(taskController.getName().getText());
        description.setText(taskController.getDescription().getText());
    }

    /**
     * abort edit
     *
     * @param event
     */
    public void cancel(ActionEvent event) {
        closeWindow(event);
    }

    public void closeWindow(Event event) {
        Utils.getStageFromEvent(event).close();
    }

    /**
     * retrieves the information entered by the user and send it to the task being edited
     *
     * @param event
     */
    public void save(Event event) {
        // clear error messages
        this.nameError.setText("");
        this.descriptionError.setText("");

        // extract info
        String newName = this.name.getText();
        String newDescription = this.description.getText();
        String newDate = this.date.getValue().format(DateTimeFormatter.ofPattern("d MMM yy"));

        // validate info
        boolean isValid = true;
        if (newName.isBlank()) {
            this.nameError.setText("Please enter a name");
            isValid = false;
        }
        if (newDescription.isBlank()) {
            this.descriptionError.setText("please enter a description");
            isValid = false;
        }

        // save changes to task controller if no errors
        if (isValid) {
            try {
                // mark for saving
                this.saveTask = true;
                this.taskController.saveChanges(newName, newDescription, newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            closeWindow(event);
        }
    }

    /**
     * call save method on enter key pressed
     *
     * @param keyEvent
     * @throws IOException
     */
    public void onEnter(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER)
            save(keyEvent);
    }


    public void setTitle(String title) {
        this.title.setText(title);
    }
}