create table if not exists USERS
(
    USER_ID       LONG auto_increment,
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

create table if not exists FRIEND_STATUS
(
    USER_ID     LONG not null references USERS(USER_ID),
    FRIEND_ID   LONG not null
);

create table if not exists GENRES
(
    GENRE_ID   INTEGER               not null,
    GENRE_NAME CHARACTER VARYING(20) not null,
    constraint GENRE_ID
        primary key (GENRE_ID)
);

create table if not exists MPA
(
    MPA_ID   INTEGER,
    MPA_NAME CHARACTER VARYING(10) not null,
    constraint MPA_ID
        primary key (MPA_ID)
);

create table if not exists FILMS
(
    FILM_ID      LONG auto_increment,
    FILM_NAME    CHARACTER VARYING(100)  not null,
    DESCRIPTION  CHARACTER VARYING(1000) not null,
    RELEASE_DATE DATE                    not null,
    DURATION     INTEGER                 not null,
    RATE         INTEGER,
    MPA_ID       INTEGER                 not null,
    constraint FILM_ID
        primary key (FILM_ID),
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

CREATE TABLE if not exists FILMS_TO_GENRES
(
    FILM_ID  LONG references FILMS(FILM_ID),
    GENRE_ID INTEGER references GENRES(GENRE_ID)
);

CREATE TABLE if not exists FILMS_TO_USERS
(
    FILM_ID  LONG references FILMS(FILM_ID),
    USER_ID LONG references USERS(USER_ID)
);



