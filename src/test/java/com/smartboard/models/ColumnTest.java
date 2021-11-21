package com.smartboard.models;

import com.smartboard.models.interfaces.Column;
import com.smartboard.models.interfaces.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColumnTest {

    Column col;
    Task t1, t2, t3;

    @BeforeEach
    void setUp() {
        col = new ColumnImpl(1, "column 1", null);

        t1 = new TaskImpl(0, "task 1", "description 1", Calendar.getInstance(),
                TaskState.ACTIVE, col, 0);
        t2 = new TaskImpl(1, "task 2", "description 2", Calendar.getInstance(),
                TaskState.ACTIVE, col, 1);
        t3 = new TaskImpl(2, "task 3", "description 3", Calendar.getInstance(),
                TaskState.ACTIVE, col, 2);

    }

    @AfterEach
    void tearDown() {
        col = null;
        t1 = null;
        t2 = null;
        t3 = null;
    }

    @Test
    void addTask() {
        List<Task> list = col.addTask(t1);
        assertEquals(1, list.size());
        assertEquals(t1, list.get(0));
        list = col.addTask(t2);
        assertEquals(2, list.size());
        assertEquals(t2, list.get(1));
        list = col.addTask(t3);
        assertEquals(3, list.size());
        assertEquals(t3, list.get(2));
    }

    @Test
    void removeTask() {
        List<Task> tasks = col.getTasks();
        tasks.addAll(Arrays.asList(t1, t2, t3));
        assertTrue(tasks.contains(t1) && tasks.contains(t2) && tasks.contains(t3));
        assertSame(tasks, col.getTasks());
        assertEquals(3, tasks.size());
        List<Task> returnTasks = col.removeTask(t1);
        assertEquals(2, returnTasks.size());
        assertTrue(!returnTasks.contains(t1) && returnTasks.contains(t2) && returnTasks.contains(t3));
        returnTasks = col.removeTask(t2);
        assertEquals(1, returnTasks.size());
        assertTrue(!returnTasks.contains(t1) && !returnTasks.contains(t2) && returnTasks.contains(t3));
        returnTasks = col.removeTask(t3);
        assertEquals(0, returnTasks.size());
        assertTrue(!returnTasks.contains(t1) && !returnTasks.contains(t2) && !returnTasks.contains(t3));
    }
}