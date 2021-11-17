package com.smartboard.models;

import com.smartboard.Utils.DBManager;
import com.smartboard.controllers.TaskController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Task implements Identifiable, Updatable, Deletable {
    private int id;
    private int index;
    private String name;
    private String description;
    private Calendar dueDate;
    private List<ListItem> listItems;
    private TaskState state;
    private Column column;
    private TaskController taskController;

    public Task() {
        this.listItems = new ArrayList<>();
    }

    public Task(String name, String description, Calendar dueDate, TaskState state, Column column) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.state = state;
        this.column = column;
        this.listItems = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void changeIndex(int index) {
        this.index = index;
        update();
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ListItem> getListItems() {
        return listItems;
    }

    public void setListItems(List<ListItem> listItems) {
        this.listItems = listItems;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    /**
     * Adds a ListItem object to the List
     *
     * @param listItem The item to add
     * @return The updated List
     */
    public List<ListItem> addListItem(ListItem listItem) {
        this.listItems.add(listItem);
        return this.listItems;
    }

    /**
     * Removes a ListItem from the list
     *
     * @param listItem The item to remove
     * @return The updated List
     */
    public List<ListItem> removeListItem(ListItem listItem) {
        this.listItems.remove(listItem);
        return this.listItems;
    }

    public void setController(TaskController taskController) {
        this.taskController = taskController;
    }

    public void update(String newName, String newDescription, Calendar newDate) {
        // todo validate

        name = newName;
        description = newDescription;
        dueDate = newDate;
        DBManager.updateTask(this);
    }

    @Override
    public boolean update() {
        return DBManager.updateTask(this);
    }

    public boolean delete() {
        return DBManager.deleteTask(this);
    }
}