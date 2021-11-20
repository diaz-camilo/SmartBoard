package com.smartboard.models;

import com.smartboard.Utils.DBManager;
import com.smartboard.controllers.ProjectController;

import java.util.ArrayList;
import java.util.List;

public class Project implements Identifiable, Updatable, Deletable {

    private int id;
    private String name;
    private List<Column> columns;
    private Workspace workSpace;
    private ProjectController controller;


//    public Project() {
//        this.columns = new ArrayList<>();
//    }
//
//

    /**
     * Constructor to be used exvlusively by the DBManager when fetching and building
     * the user's workspace from the database.
     * <p>
     * using this constructor will not trigger a database insert
     *
     * @param name      the mane of tghe project
     * @param workSpace the parent workspace of the project
     * @param id        the project's id
     */
    public Project(String name, Workspace workSpace, int id) {
        this.id = id;
        this.name = name;
        this.workSpace = workSpace;
        this.columns = new ArrayList<>();
    }

    public Project(String name, Workspace workSpace) {
        this.name = name;
        this.workSpace = workSpace;
        this.columns = new ArrayList<>();
        this.id = DBManager.createProject(this);
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
        update();
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


    @Override
    public boolean delete() {
        return DBManager.deleteProject(this);
    }

    @Override
    public boolean update() {
        return DBManager.updateProject(this);
    }
}