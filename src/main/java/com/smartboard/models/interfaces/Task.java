package com.smartboard.models.interfaces;

import com.smartboard.controllers.TaskController;
import com.smartboard.models.TaskState;

import java.util.Calendar;
import java.util.List;

public interface Task extends Identifiable, Updatable, Deletable {
    void setId(int id);

    int getIndex();

    void setIndex(int index);

    void changeIndex(int index);

    Calendar getDueDate();

    void setDueDate(Calendar dueDate);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    List<ListItem> getListItems();

    void setListItems(List<ListItem> listItems);

    TaskState getState();

    void setState(TaskState state);

    Column getColumn();

    void setColumn(Column column);

    /**
     * Adds a ListItem object to the List
     *
     * @param listItem The item to add
     * @return The updated List
     */
    List<ListItem> addListItem(ListItem listItem);

    /**
     * Removes a ListItem from the list
     *
     * @param listItem The item to remove
     * @return The updated List
     */
    List<ListItem> removeListItem(ListItem listItem);

    void setController(TaskController taskController);

    void update(String newName, String newDescription, Calendar newDate);
}