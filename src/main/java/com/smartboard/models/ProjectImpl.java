package com.smartboard.models;

import com.smartboard.Utils.DBManager;
import com.smartboard.controllers.ProjectController;
import com.smartboard.models.interfaces.Column;
import com.smartboard.models.interfaces.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectImpl implements Project {

    private int id;
    private String name;
    private List<Column> columns;
    private WorkspaceImpl workSpace;
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
    public ProjectImpl(String name, WorkspaceImpl workSpace, int id) {
        this.id = id;
        this.name = name;
        this.workSpace = workSpace;
        this.columns = new ArrayList<>();
    }

    public ProjectImpl(String name, WorkspaceImpl workSpace) {
        this.name = name;
        this.workSpace = workSpace;
        this.columns = new ArrayList<>();
        this.id = DBManager.createProject(this);
    }

    @Override
    public void setController(ProjectController controller) {
        this.controller = controller;
    }

    @Override
    public ProjectController getController() {
        return controller;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        update();
    }

    @Override
    public List<Column> getColumns() {
        return columns;
    }

    @Override
    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    @Override
    public WorkspaceImpl getWorkSpace() {
        return workSpace;
    }

    @Override
    public void setWorkSpace(WorkspaceImpl workSpace) {
        this.workSpace = workSpace;
    }

    @Override
    public List<Column> addColumn(Column column) {
        this.columns.add(column);
        return this.columns;
    }

    @Override
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