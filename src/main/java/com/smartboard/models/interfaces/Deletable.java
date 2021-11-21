package com.smartboard.models.interfaces;

public interface Deletable {
    /**
     * Deletes object from the database
     *
     * @return true if successful, false if failed
     */
    boolean delete();
}