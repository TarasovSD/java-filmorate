package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FilmService {

    @Autowired
    private FilmStorage inMemoryFilmStorage;

    private final Comparator<Film> comparator = Comparator.comparing(Film::getCountOfLikes);

    public Film createFilm(Film film) {
        inMemoryFilmStorage.createFilm(film);
        return film;
    }

    public List<Film> getFilms() {
        return inMemoryFilmStorage.getFilms();
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    public Film getFilmById(long filmId) {
        return inMemoryFilmStorage.getFilmById(filmId);
    }

    public Film likeFilm(long id, long userId) {
        Film filmToSetLike = inMemoryFilmStorage.getFilmById(id);
        validate(filmToSetLike);
        Set<Long> idsOfLikers = filmToSetLike.getIdsOfLikers();
        idsOfLikers.add(userId);
        filmToSetLike.setIdsOfLikers(idsOfLikers);
        filmToSetLike.setCountOfLikes(filmToSetLike.getCountOfLikes() + 1);
        log.info("Пользователь с ID {} поставил лайк фильму {} с ID {}", userId, filmToSetLike.getName(), id);
        return inMemoryFilmStorage.updateFilm(filmToSetLike);
    }

    public void deleteLike(long id, long userId) {
        Film filmToDeleteLike = inMemoryFilmStorage.getFilmById(id);
        validate(filmToDeleteLike);
        checkLikesCount(filmToDeleteLike);
        Set<Long> idsOfLikers = filmToDeleteLike.getIdsOfLikers();
        idsOfLikers.remove(userId);
        filmToDeleteLike.setIdsOfLikers(idsOfLikers);
        inMemoryFilmStorage.updateFilm(filmToDeleteLike);
        filmToDeleteLike.setCountOfLikes(filmToDeleteLike.getCountOfLikes() - 1);
        log.info("Пользователь с ID {} удалил лайк фильму {} с ID {}", userId, filmToDeleteLike.getName(), id);
    }

    public List<Film> getListOfFilmsByNumberOfLikes(Integer count) {
        List<Film> listOfFilms = inMemoryFilmStorage.getFilms();
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
