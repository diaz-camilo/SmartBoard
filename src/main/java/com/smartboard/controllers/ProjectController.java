package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.DBManager;
import com.smartboard.Utils.Utils;
import com.smartboard.models.Column;
import com.smartboard.models.Project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ProjectController {

    @FXML
    public HBox columnsContainer;
    @FXML
    public Tab projectTab;

    public Project model;
    public Tab view;


    public void init(Project project, Tab view) {
        this.model = project;
        this.view = view;
        this.model.setController(this);
        this.projectTab.setText(this.model.getName());
    }

    public Project getModel() {
        return model;
    }

    public void setModel(Project model) {
        this.model = model;
    }

    public Tab getView() {
        return view;
    }

    public void setView(Tab view) {
        this.view = view;
    }

    public void addColumn(Node column) {
        this.columnsContainer.getChildren().add(column);
    }

    public void removeColumn(ColumnController columnController) {
        // remove from UI
        this.columnsContainer.getChildren().remove(columnController.getView());

        // remove from DB
        DBManager.deleteColumn(columnController.getModel());
    }

    public void addColumn(ActionEvent event) throws IOException {

        String dialogPrompt = "Enter new column name";
        String errorPrompt = "Column name can not be empty.\nPlease enter a name for the new column";
        String defaultVal = "New Column";
        String columnName = Utils.getStringFromDialog(dialogPrompt, errorPrompt, defaultVal);
        if (columnName == null)
            return;

        // create column obj
        Column column = DBManager.addColumn(this.model.getId(), columnName);
        column.setProject(this.model);
        this.model.getColumns().add(column);

        // generate new column
        FXMLLoader columnLoader = new FXMLLoader(Application.class.getResource("column.fxml"));
        VBox vBoxColumn = columnLoader.load();
        ColumnController columnController = columnLoader.getController();
        columnController.init(column, vBoxColumn);

        // add column to UI
        this.columnsContainer.getChildren().add(vBoxColumn);
    }

    public void deleteProject(ActionEvent event) {
        model.getWorkSpace().getController().removeProject(this);
    }
}