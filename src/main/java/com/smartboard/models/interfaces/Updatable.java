package com.smartboard.models.interfaces;

public interface Updatable {

    /**
     * updates object in the database
     *
     * @return true if successful, false if failed
     */
    boolean update();
}