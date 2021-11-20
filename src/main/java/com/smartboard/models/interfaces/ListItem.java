package com.smartboard.models.interfaces;

import com.smartboard.models.interfaces.Task;

public interface ListItem {
    /**
     * Toggles the state of the list item
     *
     * @return the new state after toggling
     */
    boolean toggleState();

    void setId(int id);

    String getDescription();

    void setDescription(String description);

    boolean isCompleted();

    void setCompleted(boolean completed);

    Task getTask();

    void setTask(Task task);
}