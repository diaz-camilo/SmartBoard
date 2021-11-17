package com.smartboard.models;

import com.smartboard.Utils.DBManager;
import com.smartboard.controllers.ProjectController;

import java.util.ArrayList;
import java.util.List;

public class Project implements Identifiable {

    private int id;
    private String name;
    private List<Column> columns;
    private Workspace workSpace;
    private ProjectController controller;


    public Project() {
        this.columns = new ArrayList<>();
    }


    public Project(int id, String name, Workspace workSpace) {
        this();
        this.id = id;
        this.name = name;
        this.workSpace = workSpace;
    }

    public void setController(ProjectController controller) {
        this.controller = controller;
    }

    public ProjectController getController() {
        return controller;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Workspace getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(Workspace workSpace) {
        this.workSpace = workSpace;
    }

    /**
     * Adds a Column object to the List
     *
     * @param column The item to add
     * @return The updated List
     */
    public List<Column> addColumn(Column column) {
        this.columns.add(column);
        return this.columns;
    }

    /**
     * Removes a Column from the list
     *
     * @param column The item to remove
     * @return The updated List
     */
    public List<Column> removeColumn(Column column) {
        this.columns.remove(column);
        DBManager.deleteColumn(column);
        return this.columns;
    }


}