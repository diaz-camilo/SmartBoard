package com.smartboard.models;

public class ListItem implements Identifiable {

    private int id;
    private String description;
    private boolean isCompleted;
    private Task task;

    /**
     * Toggles the state of the list item
     *
     * @return the new state after toggling
     */
    public boolean toggleState() {
        isCompleted = !isCompleted;
        return isCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}