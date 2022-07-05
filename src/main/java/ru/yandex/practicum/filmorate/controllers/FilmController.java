package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InputException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {

    private HashMap<Integer, Film> films = new HashMap<>();
    private int generatorId = 1;

    @PostMapping("films")
    public Film createFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
            film.setId(generatorId);
            generatorId++;
            films.put(film.getId(), film);
            log.info("Текущее количество фильмов: {}", films.size());
        } else {
            log.info("Указана дата релиза фильма {} ранее 28.12.1895", film.getName());
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }

        return film;
    }

    @GetMapping("films")
    public List<Film> getFilms() {
        List<Film> filmsList = new ArrayList<>();
        for (Map.Entry<Integer, Film> entry : films.entrySet()) {
            filmsList.add(entry.getValue());
        }
        return filmsList;
    }

    @PutMapping("films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        for (Map.Entry<Integer, Film> entry : films.entrySet()) {
            if (film.getId() == entry.getKey()) {
                films.put(film.getId(), film);
                log.info("Фильм с ID {} обновлен!", film.getId());
            } else {
                log.info("Невозможно обновить фильм с ID {}, так как он не был добавлен в список сохраненных для просмотра фильмов!", film.getId());
                throw new InputException("Такого фильма нет в списке сохраненных для просмотра фильмов!");
            }
        }
        return film;
    }
}
