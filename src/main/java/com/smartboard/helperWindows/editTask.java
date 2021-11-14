package com.smartboard.helperWindows;

import com.smartboard.controllers.TaskController;
import com.smartboard.models.Column;
import com.smartboard.models.ListItem;
import com.smartboard.models.Task;
import com.smartboard.models.TaskState;

import java.util.Calendar;
import java.util.List;

public class editTask {
    private int id;
    private String name;
    private String description;
    private Calendar dueDate;
    private List<ListItem> listItems;
    private TaskState state;
    private Column column;
    private TaskController taskController;

    public editTask(Task task) {

    }
}