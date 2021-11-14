package com.smartboard.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    public void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void save(ActionEvent event) {
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
}