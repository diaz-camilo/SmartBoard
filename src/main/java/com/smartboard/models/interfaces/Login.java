package com.smartboard.models.interfaces;

import com.smartboard.Utils.DBManager;
import com.smartboard.exceptions.UserException;
import com.smartboard.models.interfaces.User;

public interface Login extends Updatable, Deletable {
    static boolean authenticate(String username, String password) {
        return DBManager.authenticateUser(username, password);
    }

    String encryptPassword(String password);

    void validateUsername(String username) throws UserException;

    void validatePassword(String password) throws UserException;

    String getUsername();

    void setUsername(String username) throws UserException;

    String getPasswordHash();

    void setPassword(String password) throws UserException;

    User getUser();

    void updatePassword(String newPassword) throws UserException;
}