package ru.yandex.practicum.filmorate.storeges;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmStorageTest {

    private final FilmStorage filmStorage;

    private final MPAStorage mpaStorage;

    private final GenreStorage genreStorage;

    @Test
    public void testFilmsStorage() {
        MPA mpa = mpaStorage.getMPAById(1);
        List<Genre> genres = new ArrayList<>();
        genres.add(genreStorage.getGenreById(1));
        Film filmId1 = new Film(1l, "Фильм", "Описание", LocalDate.of(1990, 1, 1), 100, 1, mpa, genres, new HashSet<>());
        filmStorage.createFilm(filmId1);

        Optional<Film> filmOptionalId1 = Optional.ofNullable(filmStorage.getFilmById(1l));

        assertThat(filmOptionalId1)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1l)
                );

        assertThat(filmOptionalId1)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Фильм")
                );

        assertThat(filmOptionalId1)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "Описание")
                );

        LocalDate testDate = LocalDate.of(1990, 1, 1);
        assertThat(filmOptionalId1)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("releaseDate", testDate)
                );

        assertThat(filmOptionalId1)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 100)
                );

        assertThat(filmOptionalId1)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("countOfLikes", 1)
                );

        assertThat(filmOptionalId1)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getMpa()).isEqualTo(mpa)
                );

        assertThat(filmOptionalId1)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getGenres()).isEqualTo(genres)
                );

        Film filmToUpdate = new Film(1l, "ФильмUpdate", "ОписаниеUpdate", LocalDate.of(1996, 1, 1), 110, 0, mpa, genres, new HashSet<>());

        filmStorage.updateFilm(filmToUpdate);

        Optional<Film> filmOptionalId1Update = Optional.ofNullable(filmStorage.getFilmById(1l));

        assertThat(filmOptionalId1Update)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1l)
                );

        assertThat(filmOptionalId1Update)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "ФильмUpdate")
                );

        assertThat(filmOptionalId1Update)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "ОписаниеUpdate")
                );

        assertThat(filmOptionalId1Update)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 110)
                );

        Film filmId2 = new Film(2l, "Фильм2", "Описание2",
                LocalDate.of(1996, 1, 1), 100, 0, mpa, genres, new HashSet<>());

        filmStorage.createFilm(filmId2);

        Optional<List<Film>> listAllFilms = Optional.ofNullable(filmStorage.getFilms());

        assertThat(listAllFilms)
                .isPresent()
                .hasValueSatisfying(filmList ->
                        assertThat(filmList).isNotNull()
                );

        assertThat(listAllFilms)
                .isPresent()
                .hasValueSatisfying(filmList ->
                        assertThat(filmList.size()).isEqualTo(2)
                );

        assertThat(listAllFilms)
                .isPresent()
                .hasValueSatisfying(filmList ->
                        assertThat(filmList.get(1)).hasFieldOrPropertyWithValue("id", 2l)
                );

        assertThat(listAllFilms)
                .isPresent()
                .hasValueSatisfying(filmList ->
                        assertThat(filmList.get(1)).hasFieldOrPropertyWithValue("name", "Фильм2")
                );

        assertThat(listAllFilms)
                .isPresent()
                .hasValueSatisfying(filmList ->
                        assertThat(filmList.get(1)).hasFieldOrPropertyWithValue("description", "Описание2")
                );

        assertThat(listAllFilms)
                .isPresent()
                .hasValueSatisfying(filmList ->
                        assertThat(filmList.get(1)).hasFieldOrPropertyWithValue("duration", 100)
                );

        LocalDate testDate2 = LocalDate.of(1996,1,1);
        assertThat(listAllFilms)
                .isPresent()
                .hasValueSatisfying(filmList ->
                        assertThat(filmList.get(1)).hasFieldOrPropertyWithValue("releaseDate", testDate2)
                );
    }
}
