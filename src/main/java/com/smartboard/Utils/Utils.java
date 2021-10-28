package com.smartboard.Utils;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class Utils {

    /**
     * Check if a string is null, empty or only white space
     *
     * @param string The string to check
     * @return true if the string is null, empty or only white space. Otherwise, false.
     */
    public static boolean NullOrEmpty(String string) {
        return (string == null || string.strip().isBlank());
    }


}