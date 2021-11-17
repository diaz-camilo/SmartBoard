package com.smartboard.controllers;

import com.smartboard.Utils.Utils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class EditTaskController {

    @FXML
    private DatePicker date;

    @FXML
    private TextArea description;

    @FXML
    private TextField name;

    private TaskController taskController;

    public void init(TaskController taskController) {
        this.taskController = taskController;
        Calendar modelCalendar = taskController.getModel().getDueDate();
        // todo get date from model
        date.setValue(
                LocalDateTime.ofInstant(
                        modelCalendar.toInstant(),
                        modelCalendar.getTimeZone().toZoneId()
                ).toLocalDate()
        );
        name.setText(taskController.getName().getText());
        description.setText(taskController.getDescription().getText());

    }

    public void closeWindow(Event event) {
        Utils.getStageFromEvent(event).close();
    }

    public void save(Event event) {
        // extract info
        String newName = this.name.getText();
        String newDescription = this.description.getText();
        String newDate = this.date.getValue().format(DateTimeFormatter.ofPattern("d MMM yy"));

        // validate info
        // todo

        // save changes to task controller
        try {
            this.taskController.saveChanges(newName, newDescription, newDate);
        } catch (ParseException e) {
            System.out.println("could not parse date");
            System.out.println(this.getClass().getSimpleName() + "::save");
        }


        closeWindow(event);
    }

    public void cancel(ActionEvent event) {
        closeWindow(event);
    }

    public void onEnter(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER)
            save(keyEvent);
    }
}