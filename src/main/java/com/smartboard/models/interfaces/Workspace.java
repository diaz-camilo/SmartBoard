package com.smartboard.models.interfaces;

import com.smartboard.Utils.DBManager;
import com.smartboard.controllers.MainApplicationController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface Workspace extends Identifiable, Updatable, Deletable {
    static Workspace getUserWorkspace(User user) {
        return DBManager.readWorkspace(user);
    }

    MainApplicationController getController();

    void setController(MainApplicationController controller);

    User getUser();

    void setUser(User user);

    void setId(int id);

    List<String> getQuotes();

    void setQuotes(List<String> quotes);

    List<Project> getProjects();

    void setProjects(List<Project> projects);

    Project getDefaultProject();

    void setDefaultProject(Project defaultProject);

    String getUsername();

    void setUsername(String username);


    /**
     * Selects a random quote form the preloaded list of quotes
     *
     * @return String array of size 2. [0] the quote, [1] The author
     */
    String[] getRandomQuote();

    void setDefault(Project project);
}