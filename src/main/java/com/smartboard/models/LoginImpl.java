package com.smartboard.models;

import com.smartboard.Utils.DBManager;
import com.smartboard.Utils.Utils;
import com.smartboard.exceptions.UserException;
import com.smartboard.models.interfaces.Deletable;
import com.smartboard.models.interfaces.Login;
import com.smartboard.models.interfaces.Updatable;
import com.smartboard.models.interfaces.User;
import org.mindrot.jbcrypt.BCrypt;


public class LoginImpl implements Updatable, Deletable, Login {
    private String username;
    private String passwordHash;
    private User user;

    public LoginImpl(String username, String password, User user) throws UserException {
        validateUsername(username);
        validatePassword(password);
        if (user == null)
            throw new UserException("User reference can not be null");
        this.username = username;
        this.passwordHash = encryptPassword(password);
        this.user = user;
    }

    @Override
    public String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public void validateUsername(String username) throws UserException {
        if (Utils.NullOrEmpty(username))
            throw new UserException("Username can not be null or empty");
        // enforce username rules below
    }

    @Override
    public void validatePassword(String password) throws UserException {
        if (Utils.NullOrEmpty(password))
            throw new UserException("password can not be null or empty");
        // enforce password rules below
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) throws UserException {
        validateUsername(username);
        this.username = username;
    }

    @Override
    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public void setPassword(String password) throws UserException {
        validatePassword(password);
        this.passwordHash = encryptPassword(password);
    }

    @Override
    public User getUser() {
        return user;
    }


    @Override
    public void updatePassword(String newPassword) throws UserException {
        this.setPassword(newPassword);
        update();
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean update() {
        return DBManager.updateLogin(this);
    }
}