package com.smartboard.models;

import com.smartboard.Utils.DBManager;
import com.smartboard.controllers.ColumnController;
import com.smartboard.controllers.ProjectController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Column implements Identifiable, Updatable, Deletable {

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

    public Column(String name, Project project) {
        this.name = name;
        this.project = project;
        this.tasks = new ArrayList<>();
        this.id = DBManager.addColumn(this);
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
        tasks.sort(Comparator.comparingInt(Task::getIndex));
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
        this.tasks.forEach(t -> t.changeIndex(this.tasks.indexOf(t)));
        return this.tasks;
    }

    public ColumnController getController() {
        return this.controller;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean update() {
        return false;
    }

    public void shiftTasks(int index, Task model) {
        this.tasks.remove(model);
        this.tasks.add(index, model);
        this.tasks.forEach(task -> task.changeIndex(this.tasks.indexOf(task)));
    }
}