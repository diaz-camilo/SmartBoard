package com.smartboard.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class WorkSpaceTest {

    private WorkSpace ws;

    @BeforeEach
    void setUp() {
        ws = new WorkSpace();
    }

    // Test might fail if the two randomly selected quotes are the same, which should not happen very often.
    @Test
    void getRandomQuote() {
        assertNotEquals(Arrays.toString(ws.getRandomQuote()), Arrays.toString(ws.getRandomQuote()));
    }
}