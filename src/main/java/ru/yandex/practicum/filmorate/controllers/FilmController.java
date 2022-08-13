package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        validate(film);
        filmService.createFilm(film);
        log.info("Фильм с названием {} и ID {} добавлен в список сохраненных для просмотра фильмов!", film.getName(), film.getId());
        return film;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/films/{filmId}")
    public Film getFilmById(@PathVariable Long filmId) {
        validateFilmId(filmId);
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        validateFilmId(film.getId());
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getListOfFilmsByNumberOfLikes(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getListOfFilmsByNumberOfLikes(count);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film likeFilm(@PathVariable long id, @PathVariable long userId) {
        return filmService.likeFilm(id, userId);
    }

    @GetMapping("/mpa")
    public List<MPA> getMPA() {
        return filmService.getMPA();
    }

    @GetMapping("/mpa/{id}")
    public MPA getMPAById(@PathVariable long id) {
        return filmService.getMPAById(id);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return filmService.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable long id) {
        return filmService.getGenreById(id);
    }

    private Film validate(Film film) {
        if (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
            return film;
        } else {
            log.info("Указана дата релиза фильма {} ранее 28.12.1895", film.getName());
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
    }

    private void validateFilmId(Long filmId) {
        if (filmId < 0) {
            log.info("В url передано отрицательное значения параметра filmId: {}", filmId);
            throw new NotFoundException("Id фильма в ссылке должно быть больше 0!");
        }
    }
}
