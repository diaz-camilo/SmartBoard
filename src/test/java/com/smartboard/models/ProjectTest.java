package com.smartboard.models;

import com.smartboard.models.interfaces.Column;
import com.smartboard.models.interfaces.Project;
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
        pj = new ProjectImpl("my project", null, 0);

        c1 = new ColumnImpl(1, "to do", pj);
        c2 = new ColumnImpl(2, "in progress", pj);
        c3 = new ColumnImpl(3, "completed", pj);
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