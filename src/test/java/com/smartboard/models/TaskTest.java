//package com.smartboard.models;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class TaskTest {
//
//    Task tk;
//    ListItem li1, li2, li3;
//
//    @BeforeEach
//    void setUp() {
//        tk = new Task();
//
//        li1 = new ListItem();
//        li2 = new ListItem();
//        li3 = new ListItem();
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void addListItem() {
//        List<ListItem> list = tk.addListItem(li1);
//        assertEquals(1, list.size());
//        assertEquals(li1, list.get(0));
//        list = tk.addListItem(li2);
//        assertEquals(2, list.size());
//        assertEquals(li2, list.get(1));
//        list = tk.addListItem(li3);
//        assertEquals(3, list.size());
//        assertEquals(li3, list.get(2));
//    }
//
//    @Test
//    void removeListItem() {
//        List<ListItem> cols = tk.getListItems();
//        cols.addAll(Arrays.asList(li1, li2, li3));
//        assertTrue(cols.contains(li1) && cols.contains(li2) && cols.contains(li3));
//        assertSame(cols, tk.getListItems());
//        assertEquals(3, cols.size());
//        List<ListItem> returnCols = tk.removeListItem(li1);
//        assertEquals(2, returnCols.size());
//        assertTrue(!returnCols.contains(li1) && returnCols.contains(li2) && returnCols.contains(li3));
//        returnCols = tk.removeListItem(li2);
//        assertEquals(1, returnCols.size());
//        assertTrue(!returnCols.contains(li1) && !returnCols.contains(li2) && returnCols.contains(li3));
//        returnCols = tk.removeListItem(li3);
//        assertEquals(0, returnCols.size());
//        assertTrue(!returnCols.contains(li1) && !returnCols.contains(li2) && !returnCols.contains(li3));
//
//    }
//}