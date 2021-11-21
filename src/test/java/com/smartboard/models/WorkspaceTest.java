package com.smartboard.models;

import com.smartboard.models.interfaces.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class WorkspaceTest {

    private Workspace ws;

    @BeforeEach
    void setUp() {
        ws = new WorkspaceImpl(0, null);
    }

    // Test might fail if the two randomly selected quotes are the same, which should not happen very often.
    @Test
    void getRandomQuote() {
        assertNotEquals(Arrays.toString(ws.getRandomQuote()), Arrays.toString(ws.getRandomQuote()));
    }
}