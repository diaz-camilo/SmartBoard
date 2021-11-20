package com.smartboard.models;

import com.smartboard.Utils.DBManager;
import com.smartboard.Utils.Utils;
import com.smartboard.exceptions.UserException;
import org.mindrot.jbcrypt.BCrypt;


public class Login implements Updatable, Deletable {
    private String username;
    private String passwordHash;
    private User user;

    public Login(String username, String password, User user) throws UserException {
        validateUsername(username);
        validatePassword(password);
        if (user == null)
            throw new UserException("User reference can not be null");
        this.username = username;
        this.passwordHash = encryptPassword(password);
        this.user = user;
    }

    private String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean authenticate(String username, String password) {
        return DBManager.authenticateUser(username, password);
    }

    private void validateUsername(String username) throws UserException {
        if (Utils.NullOrEmpty(username))
            throw new UserException("Username can not be null or empty");
        // enforce username rules below
    }

    private void validatePassword(String password) throws UserException {
        if (Utils.NullOrEmpty(password))
            throw new UserException("password can not be null or empty");
        // enforce password rules below
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws UserException {
        validateUsername(username);
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) throws UserException {
        validatePassword(password);
        this.passwordHash = encryptPassword(password);
    }

    public User getUser() {
        return user;
    }

    public boolean CheckPassword(String password) {
        return BCrypt.checkpw(password, passwordHash);
    }

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