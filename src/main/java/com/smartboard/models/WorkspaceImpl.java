package com.smartboard.models;

import com.smartboard.Utils.DBManager;
import com.smartboard.controllers.MainApplicationController;
import com.smartboard.models.interfaces.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class WorkspaceImpl implements Identifiable, Deletable, Updatable, Workspace {

    private final int id;
    private final List<String> quotes;
    private List<Project> projects;
    private Project defaultProject;
    private String username;
    private User user;
    private MainApplicationController controller;

    public WorkspaceImpl(int id, User user) {
        this.id = id;
        this.user = user;
        this.projects = new ArrayList<>();
        this.username = user.getUsername();
        this.quotes = loadQuotes();
    }

    @Override
    public MainApplicationController getController() {
        return controller;
    }

    @Override
    public void setController(MainApplicationController controller) {
        this.controller = controller;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    @Override
    public List<Project> getProjects() {
        return projects;
    }

    @Override
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public Project getDefaultProject() {
        return defaultProject;
    }

    @Override
    public void setDefaultProject(Project defaultProject) {
        this.defaultProject = defaultProject;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Attempts to load a list of quotes from file src/static/resources/txt/quotes.txt if file not found
     * returns a list of default quotes.
     *
     * @return a list of quote and author separated by '|' eg: "Foo bar" | Alice Foo
     */
    private List<String> loadQuotes() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/static/resources/txt/quotes.txt"))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // List did not load, return a list with default quotes
        return Arrays.asList(
                "“The same boiling water that softens the potato hardens the egg. It’s what you’re made of. Not the circumstances.”|Unknown",
                "“If we have the attitude that it’s going to be a great day it usually is.”|Catherine Pulsifier",
                "“If something is important enough, even if the odds are stacked against you, you should still do it.”|Elon Musk",
                "“Don’t be afraid to give up the good to go for the great.”|John D. Rockefeller"
        );
    }

    @Override
    public String[] getRandomQuote() {
        int numQuotes = this.quotes.size();
        int selected = new Random().nextInt(numQuotes);
        String wholeQuote = this.quotes.get(selected);
        return wholeQuote.split("\\|");
    }

    @Override
    public boolean delete() {
        return DBManager.deleteWorkspace(this);
    }

    @Override
    public boolean update() {
        return DBManager.updateWorkspace(this);
    }

    @Override
    public void setDefault(Project project) {
        setDefaultProject(project);
        update();
    }
}