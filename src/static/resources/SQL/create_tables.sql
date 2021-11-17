create table users
(
    username             varchar(20) not null unique primary key,
    firstname            varchar(20) not null,
    lastname             varchar(20) not null,
    profile_picture_path varchar(60)
);

create table logins
(
    username primary key unique not null references users (username)
        on delete cascade
        on update cascade,
    password_hash varchar(60)   not null
);

create table workspaces
(
    id              integer primary key unique not null,
    username                                   not null references users (username)
        on delete cascade
        on update cascade,
    default_project INTEGER
                                               references projects (id)
                                                   on update set null on delete set null
);

create table projects
(
    id   integer primary key unique not null,
    name varchar(20)                not null,
    workspace_id                    not null references workspaces (id)
        on delete cascade
        on update cascade
);

create table columns
(
    id   integer primary key unique not null,
    name varchar(20)                not null,
    project_id                      not null references projects (id)
        on delete cascade
        on update cascade
);

create table tasks
(
    id          integer primary key unique not null,
    name        varchar(20)                not null,
    description text,
    duedate     date,
    state       integer                    not null,
    column_id                              not null references columns (id)
        on delete cascade
        on update cascade, [
    index]
    INT
);

create table list_items
(
    id           integer primary key unique not null,
    description  varchar(60)                not null,
    is_completed integer                    not null,
    task_id                                 not null references tasks (id)
        on update cascade
        on delete cascade
);