package com.smartboard.models;

import com.smartboard.models.interfaces.Identifiable;
import com.smartboard.models.interfaces.ListItem;
import com.smartboard.models.interfaces.Task;

public class ListItemImpl implements Identifiable, ListItem {

    private int id;
    private String description;
    private boolean isCompleted;
    private Task task;

    @Override
    public boolean toggleState() {
        isCompleted = !isCompleted;
        return isCompleted;
    }

    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public Task getTask() {
        return task;
    }

    @Override
    public void setTask(Task task) {
        this.task = task;
    }
}