package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.DBManager;
import com.smartboard.models.Column;
import com.smartboard.models.Project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ProjectController {

    @FXML
    public HBox columnsContainer;
    @FXML
    public Tab projectTab;

    public Project model;
    public Node view;


    public void init(Project project) {
        this.model = project;
        this.model.setController(this);
        this.projectTab.setText(this.model.getName());
    }

    public Project getModel() {
        return model;
    }

    public void setModel(Project model) {
        this.model = model;
    }

    public Node getView() {
        return view;
    }

    public void setView(Node view) {
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
        // create column obj
        Column column = DBManager.addColumn(this.model.getId(), "new Column");
        column.setProject(this.model);

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