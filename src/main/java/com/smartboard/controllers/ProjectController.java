package com.smartboard.controllers;

import com.smartboard.Application;
import com.smartboard.Utils.Utils;
import com.smartboard.models.interfaces.Column;
import com.smartboard.models.ColumnImpl;
import com.smartboard.models.interfaces.Project;
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
    private HBox columnsContainer;
    @FXML
    private Tab projectTab;

    private Project model;
    private Tab view;

    /**
     * set the tab name to the model's name attribute and
     * set the model in the controller and vice versa
     *
     * @param project
     */
    public void init(Project project) {
        this.model = project;
        this.view = projectTab;
        this.model.setController(this);
        this.projectTab.setText(this.model.getName());
    }

    public void setTabName(String name) {
        this.projectTab.setText(name);
    }

    public Project getModel() {
        return model;
    }

    public Tab getView() {
        return view;
    }

    public void addColumn(Node column) {
        this.columnsContainer.getChildren().add(column);
    }

    /**
     * delete a column from the model and UI
     *
     * @param columnController the column controller containing the model and view to remove
     */
    public void removeColumn(ColumnController columnController) {
        // remove from UI
        this.columnsContainer.getChildren().remove(columnController.getView());

        // remove from model
        this.model.removeColumn(columnController.getModel());
    }

    /**
     * launch a helper window to get a name for the new column.
     * adds a new column to the model and UI
     *
     * @param event
     * @throws IOException
     */
    public void addColumn(ActionEvent event) throws IOException {

        String dialogPrompt = "Enter new column name";
        String errorPrompt = "Column name can not be empty.\nPlease enter a name for the new column";
        String defaultVal = "New Column";
        String columnName = Utils.getStringFromDialog(dialogPrompt, errorPrompt, defaultVal);
        if (columnName == null)
            return;

        // create column obj
        Column column = new ColumnImpl(columnName, this.model);
        this.model.getColumns().add(column);

        // generate new column
        FXMLLoader columnLoader = new FXMLLoader(Application.class.getResource("column.fxml"));
        VBox vBoxColumn = columnLoader.load();
        ColumnController columnController = columnLoader.getController();
        columnController.init(column);

        // add column to UI
        this.columnsContainer.getChildren().add(vBoxColumn);
    }

    /**
     * launch an input dialog to get the new name for the column.
     * updates model and UI
     *
     * @param event
     */
    public void editProjectName(ActionEvent event) {
        String dialogPrompt = "Enter new project name";
        String errorPrompt = "Project name can not be empty.\nPlease enter a name for the project";
        String defaultVal = model.getName();
        String projectName = Utils.getStringFromDialog(dialogPrompt, errorPrompt, defaultVal);
        if (projectName == null)
            return;

        // update model
        model.setName(projectName);

        // update UI
        String prefix = this.model == MainApplicationController.activeUser.getWorkSpace().getDefaultProject()
                ? "(*) " : "";
        this.projectTab.setText(prefix + projectName);
    }

    public void deleteProject(ActionEvent event) {
        model.getWorkSpace().getController().removeProject(this);
    }

    /**
     * sets this project as default.
     * updates model and UI
     *
     * @param event
     */
    public void setDefaultProject(ActionEvent event) {

        // inform workspace and reset all tabs titles(including this tab)
        this.model.getWorkSpace().getController().setDefaultProject(this);

        //Change tab title
        this.projectTab.setText("(*) " + this.model.getName());
    }


}