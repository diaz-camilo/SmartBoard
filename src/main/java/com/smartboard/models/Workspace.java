package com.smartboard.models;

import com.smartboard.Utils.DBManager;
import com.smartboard.controllers.MainApplicationController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Workspace implements Identifiable {

    private int id;
    private List<String> quotes;
    private List<Project> projects;
    private Project defaultProject;
    private String username;
    private User user;
    private MainApplicationController controller;


    public Workspace() {
        this.quotes = loadQuotes();
    }


    public Workspace(User user) {
        this.username = user.getUsername();
        this.quotes = loadQuotes();
        this.id = DBManager.getWorkSpaceId(user.getUsername());
        this.projects = DBManager.getUserProjects(this);
        List<Project> tempProjects = this.projects.parallelStream()
                .filter(x -> x.getId() == DBManager.getDefaultProject(user.getUsername()))
                .collect(Collectors.toList());
        this.defaultProject = tempProjects.size() == 0 ? null : tempProjects.get(0);
        populateProjectColumns(this.defaultProject);
    }

    public MainApplicationController getController() {
        return controller;
    }

    public void setController(MainApplicationController controller) {
        this.controller = controller;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<String> quotes) {
        this.quotes = quotes;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Project getDefaultProject() {
        return defaultProject;
    }

    public void setDefaultProject(Project defaultProject) {
        this.defaultProject = defaultProject;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private void populateProjectColumns(Project project) {
        if (project != null)
            project.setColumns(DBManager.getProjectColumns(project));
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

    /**
     * Selects a random quote form the preloaded list of quotes
     *
     * @return String array of size 2. [0] the quote, [1] The author
     */
    public String[] getRandomQuote() {
        int numQuotes = this.quotes.size();
        int selected = new Random().nextInt(numQuotes);
        String wholeQuote = this.quotes.get(selected);
        return wholeQuote.split("\\|");
    }
}