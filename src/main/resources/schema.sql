create table STATUSES
(
    STATUS_ID     INTEGER auto_increment,
    STATUS_NAME   CHARACTER VARYING(16),
    constraint STATUS_ID
        primary key (STATUS_ID)
);

create table USERS
(
    USER_ID       INTEGER auto_increment,
    USER_EMAIL    CHARACTER VARYING(200),
    LOGIN         CHARACTER VARYING(50)  not null,
    USER_NAME     CHARACTER VARYING(100) not null,
    BIRTHDAY      DATE                   not null,
    constraint USER_ID
        primary key (USER_ID)
);

create unique index USERS_LOGIN_UNQ
    on USERS (LOGIN);

create unique index USERS_EMAIL_UNQ
    on USERS (USER_EMAIL);

create table GENRES
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(20) not null
);

create unique index GENRE_ID_UNQ
    on GENRES (GENRE_ID);

create unique index GENRE_NAME_UNQ
    on GENRES (GENRE_ID);

alter table GENRES
    add constraint GENRE_ID
        primary key (GENRE_ID);

create table MPA
(
    MPA_ID   INTEGER auto_increment,
    MPA_NAME CHARACTER VARYING(10) not null,
    constraint MPA_ID
        primary key (MPA_ID)
);

create table FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    CHARACTER VARYING(100)  not null,
    DESCRIPTION  CHARACTER VARYING(1000) not null,
    RELEASE_DATE DATE                    not null,
    DURATION     INTEGER                 not null,
    RATE         INTEGER,
    GENRE_ID     INTEGER                 not null,
    MPA_ID       INTEGER                 not null,
    constraint FILM_ID
        primary key (FILM_ID),
    constraint FILMS_GENRES_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRES,
    constraint FILMS_MPA_GENRE_ID_FK
        foreign key (MPA_ID) references MPA
);