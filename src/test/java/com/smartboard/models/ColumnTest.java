//package com.smartboard.models;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ColumnTest {
//
//    Column col;
//    Task t1, t2, t3;
//
//    @BeforeEach
//    void setUp() {
////        col = new Column();
//
//        t1 = new Task();
//        t1.setColumn(col);
//        t1.setName("task 1");
//        t1.setDescription("Description 1");
//        t1.setDueDate(Calendar.getInstance());
//        t1.setState(TaskState.ACTIVE);
//
//        t2 = new Task();
//        t2.setColumn(col);
//        t2.setName("task 2");
//        t2.setDescription("Description 2");
//        t2.setDueDate(Calendar.getInstance());
//        t2.setState(TaskState.ACTIVE);
//
//        t3 = new Task();
//        t3.setColumn(col);
//        t3.setName("task 3");
//        t3.setDescription("Description 3");
//        t3.setDueDate(Calendar.getInstance());
//        t3.setState(TaskState.ACTIVE);
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void addTask() {
//        List<Task> list = col.addTask(t1);
//        assertEquals(1, list.size());
//        assertEquals(t1, list.get(0));
//        list = col.addTask(t2);
//        assertEquals(2, list.size());
//        assertEquals(t2, list.get(1));
//        list = col.addTask(t3);
//        assertEquals(3, list.size());
//        assertEquals(t3, list.get(2));
//    }
//
//    @Test
//    void removeTask() {
//        List<Task> tasks = col.getTasks();
//        tasks.addAll(Arrays.asList(t1, t2, t3));
//        assertTrue(tasks.contains(t1) && tasks.contains(t2) && tasks.contains(t3));
//        assertSame(tasks, col.getTasks());
//        assertEquals(3, tasks.size());
//        List<Task> returnTasks = col.removeTask(t1);
//        assertEquals(2, returnTasks.size());
//        assertTrue(!returnTasks.contains(t1) && returnTasks.contains(t2) && returnTasks.contains(t3));
//        returnTasks = col.removeTask(t2);
//        assertEquals(1, returnTasks.size());
//        assertTrue(!returnTasks.contains(t1) && !returnTasks.contains(t2) && returnTasks.contains(t3));
//        returnTasks = col.removeTask(t3);
//        assertEquals(0, returnTasks.size());
//        assertTrue(!returnTasks.contains(t1) && !returnTasks.contains(t2) && !returnTasks.contains(t3));
//
//    }
//}