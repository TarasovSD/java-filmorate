package ru.yandex.practicum.filmorate.storage;

import java.util.Set;

public interface LikesStorage {
    Set<Long> likeFilm(long id, long userId);

    Set<Long> deleteLikeFilm(long id, long userId);
}
