package com.smartboard.Utils;

import com.smartboard.controllers.TaskController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
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

    /**
     * generates a text input pop-up window for the user
     *
     * @param prompt       the prompt the user will see
     * @param errorPrompt  the prompt to display if the user enters an empty string or just white space
     * @param defaultValue the default value to display on the text input field
     * @return the string the user entered or null if the user cancels
     */
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

    /**
     * Returns the Stage object where the event originated
     *
     * @param event the event
     * @return the stage where the event originated
     */
    public static Stage getStageFromEvent(Event event) {
        Node source = (Node) event.getSource();
        return (Stage) source.getScene().getWindow();
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