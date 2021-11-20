package com.smartboard.models;

import com.smartboard.Utils.DBManager;
import com.smartboard.Utils.Utils;
import com.smartboard.exceptions.UserException;
import com.smartboard.models.interfaces.Login;
import com.smartboard.models.interfaces.User;
import com.smartboard.models.interfaces.Workspace;

public class UserImpl implements User {

    private String username;
    private String firstName;
    private String lastName;
    private String profilePicturePath;
    private Login login;
    private Workspace workSpace;


    public UserImpl() {
        workSpace = new WorkspaceImpl();
    }

    public UserImpl(String firstName, String lastName, String username, String password, String profilePicturePath) throws UserException {
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
        this.login = new LoginImpl(username, password, this);
        workSpace = new WorkspaceImpl();
        DBManager.createUser(this);
    }


    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) throws UserException {
        if (Utils.NullOrEmpty(firstName))
            throw new UserException("First name can not be null or empty");
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) throws UserException {
        if (Utils.NullOrEmpty(lastName))
            throw new UserException("Last name can not be null or empty");
        this.lastName = lastName;
    }

    @Override
    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    @Override
    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    @Override
    public Login getLogin() {
        return login;
    }

    @Override
    public Workspace getWorkSpace() {
        return workSpace;
    }

    @Override
    public void setWorkSpace(Workspace workSpace) {
        this.workSpace = workSpace;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
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

    @Override
    public void updateDetails(String firstname, String lastname, String profilePicPath) throws UserException {
        setFirstName(firstname);
        setLastName(lastname);
        setProfilePicturePath(profilePicPath);
        update();
    }
}