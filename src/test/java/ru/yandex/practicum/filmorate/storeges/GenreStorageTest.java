package ru.yandex.practicum.filmorate.storeges;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GenreStorageTest {

    private final GenreStorage genreStorage;

    @Test
    public void testGenreStorage() {

        Optional<Genre> genreOptionalId1 = Optional.ofNullable(genreStorage.getGenreById(1));

        assertThat(genreOptionalId1)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1)
                );

        assertThat(genreOptionalId1)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия")
                );

        Optional<Genre> genreOptionalId2 = Optional.ofNullable(genreStorage.getGenreById(2));

        assertThat(genreOptionalId2)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 2)
                );

        assertThat(genreOptionalId2)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Драма")
                );

        Optional<Genre> genreOptionalId3 = Optional.ofNullable(genreStorage.getGenreById(3));

        assertThat(genreOptionalId3)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 3)
                );

        assertThat(genreOptionalId3)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Мультфильм")
                );

        Optional<Genre> genreOptionalId4 = Optional.ofNullable(genreStorage.getGenreById(4));

        assertThat(genreOptionalId4)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 4)
                );

        assertThat(genreOptionalId4)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Триллер")
                );

        Optional<Genre> genreOptionalId5 = Optional.ofNullable(genreStorage.getGenreById(5));

        assertThat(genreOptionalId5)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 5)
                );

        assertThat(genreOptionalId5)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Документальный")
                );

        Optional<Genre> genreOptionalId6 = Optional.ofNullable(genreStorage.getGenreById(6));

        assertThat(genreOptionalId6)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 6)
                );

        assertThat(genreOptionalId6)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Боевик")
                );

        Optional<List<Genre>> allGenresOptional = Optional.ofNullable(genreStorage.getGenres());

        assertThat(allGenresOptional)
                .isPresent()
                .hasValueSatisfying(genres ->
                        assertThat(genres.size()).isEqualTo(6)
                );

        assertThat(allGenresOptional)
                .isPresent()
                .hasValueSatisfying(genres ->
                        assertThat(genres.get(0)).hasFieldOrPropertyWithValue("id", 1)
                );

        assertThat(allGenresOptional)
                .isPresent()
                .hasValueSatisfying(genres ->
                        assertThat(genres.get(0)).hasFieldOrPropertyWithValue("name", "Комедия")
                );

        assertThat(allGenresOptional)
                .isPresent()
                .hasValueSatisfying(genres ->
                        assertThat(genres.get(5)).hasFieldOrPropertyWithValue("id", 6)
                );

        assertThat(allGenresOptional)
                .isPresent()
                .hasValueSatisfying(genres ->
                        assertThat(genres.get(5)).hasFieldOrPropertyWithValue("name", "Боевик")
                );

    }
}
