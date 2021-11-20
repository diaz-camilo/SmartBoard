package com.smartboard.models;

import com.smartboard.Utils.DBManager;
import com.smartboard.controllers.ColumnController;
import com.smartboard.models.interfaces.Column;
import com.smartboard.models.interfaces.Project;
import com.smartboard.models.interfaces.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ColumnImpl implements Column {

    private int id;
    private String name;
    private List<Task> tasks;
    private Project project;
    private ColumnController controller;

    /**
     * constructor used by the DBManager when building the workspace from the database
     *
     * @param id      the column id
     * @param name    the column name
     * @param project the parent project
     */
    public ColumnImpl(int id, String name, Project project) {
        this.name = name;
        this.project = project;
        this.tasks = new ArrayList<>();
        this.id = id;
    }

    /**
     * creates a column with the given name and associates it with the given project and with an empty list of tasks
     *
     * @param name    the column name
     * @param project the parent project
     */
    public ColumnImpl(String name, Project project) {
        this.name = name;
        this.project = project;
        this.tasks = new ArrayList<>();
        this.id = DBManager.createColumn(this);
    }

    @Override
    public int getId() {
        return id;
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
    public List<Task> getTasks() {
        tasks.sort(Comparator.comparingInt(Task::getIndex));
        return tasks;
    }


    @Override
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public void setController(ColumnController controller) {
        this.controller = controller;
    }


    @Override
    public List<Task> addTask(Task task) {
        this.tasks.add(task);
        return this.tasks;
    }

    @Override
    public void shiftTasks(int index, Task model) {
        this.tasks.remove(model);
        this.tasks.add(index, model);
        this.tasks.forEach(task -> task.changeIndex(this.tasks.indexOf(task)));
    }

    @Override
    public List<Task> removeTask(Task task) {
        this.tasks.remove(task);
        this.tasks.forEach(t -> t.changeIndex(this.tasks.indexOf(t)));
        return this.tasks;
    }

    @Override
    public ColumnController getController() {
        return this.controller;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean update() {
        return DBManager.updateColumn(this);
    }


}