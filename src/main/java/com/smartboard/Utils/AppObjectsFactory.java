package com.smartboard.Utils;

import com.smartboard.models.*;

public class AppObjectsFactory {


    public static WorkSpace getWorkspace(User user) {
        WorkSpace workSpace = new WorkSpace();

        return workSpace;
    }

    public static Project getProject(WorkSpace workSpace) {
        Project project = new Project();
        return project;
    }

    public static Column getColumn(Project project) {
        Column column = new Column();
        return column;
    }

    public static Task getTask(Column column) {
        Task task = new Task();
        return task;
    }

    public static ListItem getListItem(Task task) {
        ListItem item = new ListItem();
        return item;
    }

}