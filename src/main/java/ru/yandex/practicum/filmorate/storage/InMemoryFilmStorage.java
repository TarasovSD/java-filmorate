package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> films = new HashMap<>();
    private int generatorId = 1;

    @Override
    public Film createFilm(Film film) {
        film.setId(generatorId);
        generatorId++;
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> filmsList = new ArrayList<>();
        for (Map.Entry<Long, Film> entry : films.entrySet()) {
            filmsList.add(entry.getValue());
        }
        return filmsList;
    }

    @Override
    public Film updateFilm(Film film) {
        for (Map.Entry<Long, Film> entry : films.entrySet()) {
            if (film.getId() == entry.getKey()) {
                films.put(film.getId(), film);
                log.info("Фильм с ID {} обновлен!", film.getId());
            }
        }
        return film;
    }

    @Override
    public Film getFilmById(long filmId) {
       long i = -1;
        for (long filmIdFromMap : films.keySet()) {
            if (filmId == filmIdFromMap) {
                i = filmId;
            }
        }
        if (i >= 0) {
            return films.get(i);
        } else {
            log.info("Фильма с ID {} нет в списке сохраненных для просмотра фильмов!", filmId);
            throw new NotFoundException("Такого фильма нет в списке сохраненных для просмотра фильмов!");
        }
    }
}
