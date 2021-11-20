package com.smartboard.models.interfaces;

import com.smartboard.Utils.DBManager;
import com.smartboard.exceptions.UserException;

public interface User extends Updatable, Deletable {
    static User build(String username) {
        return DBManager.readUser(username);
    }

    String getFirstName();

    void setFirstName(String firstName) throws UserException;

    String getLastName();

    void setLastName(String lastName) throws UserException;

    String getProfilePicturePath();

    void setProfilePicturePath(String profilePicturePath);

    Login getLogin();

    Workspace getWorkSpace();

    void setWorkSpace(Workspace workSpace);

    String getUsername();

    void setUsername(String username);

    void updateDetails(String firstname, String lastname, String profilePicPath) throws UserException;
}