package com.smartboard.models;

import com.smartboard.controllers.ColumnController;

import java.util.ArrayList;
import java.util.List;

public class Column implements Identifiable {

    private int id;
    private String name;
    private List<Task> tasks;
    private Project project;
    private ColumnController controller;

    public Column() {
        this.tasks = new ArrayList<>();
    }

    public Column(int id, String name, Project project) {
        this();
        this.id = id;
        this.name = name;
        this.project = project;
    }

    public Column(int id) {
        this.id = id;
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setController(ColumnController controller) {
        this.controller = controller;
    }


    /**
     * Adds a Task object to the List
     *
     * @param task The item to add
     * @return The updated List
     */
    public List<Task> addTask(Task task) {
        this.tasks.add(task);
        return this.tasks;
    }

    /**
     * Removes a Task from the list
     *
     * @param task The item to remove
     * @return The updated List
     */
    public List<Task> removeTask(Task task) {
        this.tasks.remove(task);
        return this.tasks;
    }

    public ColumnController getController() {
        return this.controller;
    }
}