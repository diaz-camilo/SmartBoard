package com.smartboard.models;

import com.smartboard.Utils.Utils;
import com.smartboard.exceptions.UserException;

public class User {

    private String usermane;
    private String firstName;
    private String lastName;
    private String profilePicturePath = "default_profile_pic.png";
    private Login login;
    private WorkSpace workSpace;

    public User() {
    }

    public User(String firstName, String lastName, String username, String password) throws UserException {
        if (Utils.NullOrEmpty(firstName))
            throw new UserException("First name can not be null or empty");
        if (Utils.NullOrEmpty(lastName))
            throw new UserException("Last name can not be null or empty");
        this.firstName = firstName;
        this.lastName = lastName;
        this.usermane = username;
        this.login = new Login(username, password, this);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws UserException {
        if (Utils.NullOrEmpty(firstName))
            throw new UserException("First name can not be null or empty");
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws UserException {
        if (Utils.NullOrEmpty(lastName))
            throw new UserException("Last name can not be null or empty");
        this.lastName = lastName;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public WorkSpace getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(WorkSpace workSpace) {
        this.workSpace = workSpace;
    }

    public String getUsermane() {
        return usermane;
    }

    public void setUsermane(String usermane) {
        this.usermane = usermane;
    }
}