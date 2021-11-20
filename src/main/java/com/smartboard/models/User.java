package com.smartboard.models;

import com.smartboard.Utils.DBManager;
import com.smartboard.Utils.Utils;
import com.smartboard.exceptions.UserException;

public class User implements Updatable, Deletable {

    private String username;
    private String firstName;
    private String lastName;
    private String profilePicturePath;
    private Login login;
    private Workspace workSpace;


    public User() {
        workSpace = new Workspace();
    }

    public User(String firstName, String lastName, String username, String password, String profilePicturePath) throws UserException {
        String errorMessage = "";
        if (Utils.NullOrEmpty(firstName))
            errorMessage += "> First Name can not be empty\n";
        if (Utils.NullOrEmpty(lastName))
            errorMessage += "> Last Name can not be empty\n";
        if (Utils.NullOrEmpty(username))
            errorMessage += "> Username can not be empty\n";
        if (Utils.NullOrEmpty(password))
            errorMessage += "> Password can not be empty\n";
        if (!errorMessage.isBlank())
            throw new UserException(errorMessage.substring(0, errorMessage.length() - 1));

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.profilePicturePath = profilePicturePath != null && !profilePicturePath.isBlank() ?
                profilePicturePath : this.profilePicturePath;
        this.login = new Login(username, password, this);
        workSpace = new Workspace();
        DBManager.createUser(this);
    }

    public static User build(String username) {
        return DBManager.readUser(username);
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

    public Workspace getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(Workspace workSpace) {
        this.workSpace = workSpace;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean delete() {
        return DBManager.deleteUser(this);
    }

    @Override
    public boolean update() {
        return DBManager.updateUser(this);
    }

    public void updateDetails(String firstname, String lastname, String profilePicPath) throws UserException {
        setFirstName(firstname);
        setLastName(lastname);
        setProfilePicturePath(profilePicPath);
        update();
    }
}