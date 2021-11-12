package com.smartboard.controllers;

import com.smartboard.Utils.DBManager;
import com.smartboard.models.Project;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;

public class ProjectController {

    @FXML
    public HBox columnsContainer;
    @FXML
    public Tab projectTab;

    public Project project;


    public void init(Project project) {
        this.project = project;
        this.project.setController(this);
        this.projectTab.setText(this.project.getName());
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
}