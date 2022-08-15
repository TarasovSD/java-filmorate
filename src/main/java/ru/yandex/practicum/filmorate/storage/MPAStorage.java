package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MPAStorage {

    List<MPA> getMPAs();

    MPA getMPAById(long id);
}
