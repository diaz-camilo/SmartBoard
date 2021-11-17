CREATE TABLE columns
(
    id   INTEGER PRIMARY KEY
        UNIQUE
                     NOT NULL,
    name VARCHAR(20) NOT NULL,
    project_id       NOT NULL
        REFERENCES projects (id) ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE list_items
(
    id           INTEGER PRIMARY KEY
        UNIQUE
                             NOT NULL,
    description  VARCHAR(60) NOT NULL,
    is_completed INTEGER     NOT NULL,
    task_id                  NOT NULL
        REFERENCES tasks (id) ON UPDATE CASCADE
            ON DELETE CASCADE
);

CREATE TABLE logins
(
    username PRIMARY KEY
        UNIQUE
                              NOT NULL
        REFERENCES users (username) ON DELETE CASCADE
            ON UPDATE CASCADE,
    password_hash VARCHAR(60) NOT NULL
);

CREATE TABLE projects
(
    id   INTEGER PRIMARY KEY
        UNIQUE
                     NOT NULL,
    name VARCHAR(20) NOT NULL,
    workspace_id     NOT NULL
        REFERENCES workspaces (id) ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE tasks
(
    id          INTEGER PRIMARY KEY
        UNIQUE
                            NOT NULL,
    name        VARCHAR(20) NOT NULL,
    description TEXT,
    duedate     DATE,
    state       VARCHAR(20) NOT NULL,
    column_id               NOT NULL
        REFERENCES columns (id) ON DELETE CASCADE
            ON UPDATE CASCADE,
    [index]     INT
);

CREATE TABLE users
(
    username             VARCHAR(20) NOT NULL
        UNIQUE
        PRIMARY KEY,
    firstname            VARCHAR(20) NOT NULL,
    lastname             VARCHAR(20) NOT NULL,
    profile_picture_path VARCHAR(60)
);

CREATE TABLE workspaces
(
    id              INTEGER PRIMARY KEY
        UNIQUE
                            NOT NULL,
    username                NOT NULL
        REFERENCES users (username) ON DELETE CASCADE
            ON UPDATE CASCADE,
    default_project INTEGER REFERENCES projects (id) ON DELETE SET NULL
                                ON UPDATE SET NULL
);