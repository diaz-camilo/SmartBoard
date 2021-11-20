package com.smartboard.models.interfaces;

import com.smartboard.controllers.ColumnController;

import java.util.List;

public interface Column extends Identifiable, Updatable, Deletable {

    String getName();

    /**
     * change the name of the column, updates the database
     *
     * @param name the new name
     */
    void setName(String name);

    /**
     * returns the list of tasks ordered by its index
     *
     * @return an ordered list of tasks
     */
    List<Task> getTasks();

    void setTasks(List<Task> tasks);

    Project getProject();

    void setProject(Project project);

    void setController(ColumnController controller);

    /**
     * Adds a Task object to the List
     *
     * @param task The item to add
     * @return The updated List
     */
    List<Task> addTask(Task task);

    /**
     * inserts the task at the specified index, shift tasks one position to the right and update all their indexes
     *
     * @param index
     * @param model
     */
    void shiftTasks(int index, Task model);

    /**
     * Removes a Task from the list
     *
     * @param task The item to remove
     * @return The updated List
     */
    List<Task> removeTask(Task task);

    ColumnController getController();
}