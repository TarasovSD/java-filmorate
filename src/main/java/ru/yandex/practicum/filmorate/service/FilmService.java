package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.DAO.LikesDBStorage;
import ru.yandex.practicum.filmorate.storage.DAO.MPADBStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.*;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    private final MPAStorage mpaStorage;

    private final GenreStorage genreStorage;

    private final LikesStorage likesStorage;

    private final Comparator<Film> comparator = Comparator.comparing(Film::getCountOfLikes);

    @Autowired
    public FilmService(FilmStorage filmStorage, MPADBStorage mpaDBStorage, GenreStorage genreStorage, LikesDBStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaDBStorage;
        this.genreStorage = genreStorage;
        this.likesStorage = likesStorage;
    }

    public Film createFilm(Film film) {
        filmStorage.createFilm(film);
        return film;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Film likeFilm(long id, long userId) {
        Film filmToSetLike = filmStorage.getFilmById(id);
        validate(filmToSetLike);
        Set<Long> idsOfLikers = likesStorage.likeFilm(id, userId);
        idsOfLikers.add(userId);
        filmToSetLike.setIdsOfLikers(idsOfLikers);
        filmToSetLike.setCountOfLikes(filmToSetLike.getCountOfLikes() + 1);
        log.info("Пользователь с ID {} поставил лайк фильму {} с ID {}", userId, filmToSetLike.getName(), id);
        return filmStorage.updateFilm(filmToSetLike);
    }

    public void deleteLike(long id, long userId) {
        Film filmToDeleteLike = filmStorage.getFilmById(id);
        validate(filmToDeleteLike);
        checkLikesCount(filmToDeleteLike);

        likesStorage.deleteLikeFilm(id, userId);

        Set<Long> idsOfLikers = filmToDeleteLike.getIdsOfLikers();
        filmToDeleteLike.setIdsOfLikers(idsOfLikers);
        filmStorage.updateFilm(filmToDeleteLike);
        filmToDeleteLike.setCountOfLikes(filmToDeleteLike.getCountOfLikes() - 1);
        log.info("Пользователь с ID {} удалил лайк фильму {} с ID {}", userId, filmToDeleteLike.getName(), id);
    }

    public List<MPA> getMPA() {
        return mpaStorage.getMPAs();
    }

    public MPA getMPAById(long id) {
        return mpaStorage.getMPAById(id);
    }

    public List<Genre> getGenres() {
        return genreStorage.getGenres();
    }

    public Genre getGenreById(long id) {
        return genreStorage.getGenreById(id);
    }

    public List<Film> getListOfFilmsByNumberOfLikes(Integer count) {
        List<Film> listOfFilms = filmStorage.getFilms();
        listOfFilms.sort(comparator);
        Collections.reverse(listOfFilms);
        if (listOfFilms.size() > count) {
            listOfFilms = listOfFilms.subList(0, listOfFilms.size() - count);
        }
        return listOfFilms;
    }

    private Film validate(Film film) {
        if (film != null) {
            return film;
        } else {
            log.info("Такого фильма нет в списке сохраненных для просмотра фильмов!");
            throw new NotFoundException("Такого фильма нет в списке сохраненных для просмотра фильмов!");
        }
    }

    private Film checkLikesCount(Film film) {
        if (film.getCountOfLikes() <= 0) {
            log.info("Невозможно убрать лайк фильма c ID {}, так как количество лайков равно нулю", film.getId());
            throw new NotFoundException("Невозможно убрать лайк, так как количество лайков равно нулю");
        }
        return film;
    }
}
