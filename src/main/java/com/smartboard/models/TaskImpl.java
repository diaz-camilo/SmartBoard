package com.smartboard.models;

import com.smartboard.Utils.DBManager;
import com.smartboard.controllers.TaskController;
import com.smartboard.models.interfaces.Column;
import com.smartboard.models.interfaces.ListItem;
import com.smartboard.models.interfaces.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskImpl implements Task {
    private int id;
    private int index;
    private String name;
    private String description;
    private Calendar dueDate;
    private List<ListItem> listItems;
    private TaskState state;
    private Column column;
    private TaskController taskController;

    public TaskImpl() {
        this.listItems = new ArrayList<>();
    }

    public TaskImpl(String name, String description, Calendar dueDate, TaskState state, Column column, int index) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.state = state;
        this.column = column;
        this.index = index;
        this.listItems = new ArrayList<>();
        this.id = DBManager.createTask(this);
    }

    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void changeIndex(int index) {
        this.index = index;
        update();
    }

    @Override
    public Calendar getDueDate() {
        return dueDate;
    }

    @Override
    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
    public List<ListItem> getListItems() {
        return listItems;
    }

    @Override
    public void setListItems(List<ListItem> listItems) {
        this.listItems = listItems;
    }

    @Override
    public TaskState getState() {
        return state;
    }

    @Override
    public void setState(TaskState state) {
        this.state = state;
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public void setColumn(Column column) {
        this.column = column;
    }

    @Override
    public List<ListItem> addListItem(ListItem listItem) {
        this.listItems.add(listItem);
        return this.listItems;
    }

    @Override
    public List<ListItem> removeListItem(ListItem listItem) {
        this.listItems.remove(listItem);
        return this.listItems;
    }

    @Override
    public void setController(TaskController taskController) {
        this.taskController = taskController;
    }

    @Override
    public void update(String newName, String newDescription, Calendar newDate) {

        name = newName;
        description = newDescription;
        dueDate = newDate;
        update();
    }

    @Override
    public boolean update() {
        return DBManager.updateTask(this);
    }

    public boolean delete() {
        return DBManager.deleteTask(this);
    }
}