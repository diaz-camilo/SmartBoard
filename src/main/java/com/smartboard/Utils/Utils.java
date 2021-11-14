package com.smartboard.Utils;

import com.smartboard.controllers.TaskController;
import javafx.event.ActionEvent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
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

    public static String getStringFromDialog(String prompt, String errorPrompt, String defaultValue) {
        //show dialog to get user input
        TextInputDialog textInputDialog = new TextInputDialog(defaultValue);
        do {
            textInputDialog.setHeaderText(prompt);
            textInputDialog.showAndWait();
            prompt = errorPrompt;
        } while (textInputDialog.getResult() != null && textInputDialog.getResult().strip().isBlank());

        String result = textInputDialog.getResult();
        return result == null ? result : result.strip();
    }


    // Helper methods to assist drag and drop task
    private static Object trackingObj;

    public static void setDraggingObj(Object trackingObj) {
        Utils.trackingObj = trackingObj;
    }

    public static Object getDraggingObj() {
        return trackingObj;
    }
}