package com.smartboard.models.interfaces;

import com.smartboard.controllers.ProjectController;
import com.smartboard.models.WorkspaceImpl;

import java.util.List;

public interface Project extends Identifiable, Updatable, Deletable {
    void setController(ProjectController controller);

    ProjectController getController();

    String getName();

    void setName(String name);

    List<Column> getColumns();

    void setColumns(List<Column> columns);

    WorkspaceImpl getWorkSpace();

    void setWorkSpace(WorkspaceImpl workSpace);

    /**
     * Adds a Column object to the List
     *
     * @param column The item to add
     * @return The updated List
     */
    List<Column> addColumn(Column column);

    /**
     * Removes a Column from the list
     *
     * @param column The item to remove
     * @return The updated List
     */
    List<Column> removeColumn(Column column);
}