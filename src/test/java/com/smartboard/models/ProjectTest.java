package com.smartboard.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    Project pj;
    Column c1, c2, c3;

    @BeforeEach
    void setUp() {
//        pj = new Project();

        c1 = new Column();
        c1.setId(1);
        c1.setName("to do");
        c1.setProject(pj);

        c2 = new Column();
        c2.setId(2);
        c2.setName("in progress");
        c2.setProject(pj);

        c3 = new Column();
        c3.setId(3);
        c3.setName("completed");
        c3.setProject(pj);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addColumn() {
        List<Column> list = pj.addColumn(c1);
        assertEquals(1, list.size());
        assertEquals(c1, list.get(0));
        list = pj.addColumn(c2);
        assertEquals(2, list.size());
        assertEquals(c2, list.get(1));
        list = pj.addColumn(c3);
        assertEquals(3, list.size());
        assertEquals(c3, list.get(2));
    }

    @Test
    void removeColumn() {
        List<Column> cols = pj.getColumns();
        cols.addAll(Arrays.asList(c1, c2, c3));
        assertTrue(cols.contains(c1) && cols.contains(c2) && cols.contains(c3));
        assertSame(cols, pj.getColumns());
        assertEquals(3, cols.size());
        List<Column> returnCols = pj.removeColumn(c1);
        assertEquals(2, returnCols.size());
        assertTrue(!returnCols.contains(c1) && returnCols.contains(c2) && returnCols.contains(c3));
        returnCols = pj.removeColumn(c2);
        assertEquals(1, returnCols.size());
        assertTrue(!returnCols.contains(c1) && !returnCols.contains(c2) && returnCols.contains(c3));
        returnCols = pj.removeColumn(c3);
        assertEquals(0, returnCols.size());
        assertTrue(!returnCols.contains(c1) && !returnCols.contains(c2) && !returnCols.contains(c3));

    }
}