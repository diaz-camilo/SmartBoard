package com.smartboard.models;

import com.smartboard.Utils.DBManager;
import com.smartboard.controllers.MainApplicationController;
import com.smartboard.models.interfaces.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class WorkspaceImpl implements Identifiable, Deletable, Updatable, Workspace {

    private int id;
    private List<String> quotes;
    private List<Project> projects;
    private Project defaultProject;
    private String username;
    private User user;
    private MainApplicationController controller;


    public WorkspaceImpl() {
        this.quotes = loadQuotes();
    }


//    public Workspace(User user) {
//        this.username = user.getUsername();
//        this.quotes = loadQuotes();
//        this.id = DBManager.readWorkspaceId(user.getUsername());
//        this.projects = DBManager.readUserProjects(this);
//        List<Project> tempProjects = this.projects.parallelStream()
//                .filter(x -> x.getId() == DBManager.readDefaultProject(user.getUsername()))
//                .collect(Collectors.toList());
//        this.defaultProject = tempProjects.size() == 0 ? null : tempProjects.get(0);
//        populateProjectColumns(this.defaultProject);
//    }

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
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public List<String> getQuotes() {
        return quotes;
    }

    @Override
    public void setQuotes(List<String> quotes) {
        this.quotes = quotes;
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
        return false;
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