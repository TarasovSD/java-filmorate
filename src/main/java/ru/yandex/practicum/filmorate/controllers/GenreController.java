package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreServise;

import java.util.List;

@RestController
@RequestMapping
public class GenreController {

    private final GenreServise genreServise;

    public GenreController(GenreServise genreServise) {
        this.genreServise = genreServise;
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return genreServise.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable long id) {
        return genreServise.getGenreById(id);
    }
}
