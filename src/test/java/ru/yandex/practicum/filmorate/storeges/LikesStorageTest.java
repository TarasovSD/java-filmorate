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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LikesStorageTest {

    private final MPAStorage mpaStorage;

    private final GenreStorage genreStorage;

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    private final LikesStorage likesStorage;

    @Test
    public void testLikesStorage() {
        MPA mpa = mpaStorage.getMPAById(1);
        List<Genre> genres = new ArrayList<>();
        genres.add(genreStorage.getGenreById(1));
        Film filmId1 = new Film(1l, "Фильм", "Описание", LocalDate.of(1990, 1, 1), 100, 1, mpa, genres, new HashSet<>());
        filmStorage.createFilm(filmId1);

        Optional<Film> filmOptionalId1 = Optional.ofNullable(filmStorage.getFilmById(1l));

        userStorage.createUser(new User(1l, "test@ya.ru", "testLogin", "Sergey",
                LocalDate.of(1990, 01, 01)));

        Optional<User> userOptionalUserId1 = Optional.ofNullable(userStorage.getUserById(1l));

        likesStorage.likeFilm(1, 1);

        Optional<Film> filmOptionalId1WithLikes = Optional.ofNullable(filmStorage.getFilmById(1l));

        assertThat(filmOptionalId1WithLikes)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getIdsOfLikers().size()).isEqualTo(1)
                );

        assertThat(filmOptionalId1WithLikes)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getIdsOfLikers().contains(1l)).isEqualTo(true)
                );

        likesStorage.deleteLikeFilm(1, 1);

        Optional<Film> filmOptionalId1WithoutLikes = Optional.ofNullable(filmStorage.getFilmById(1l));

        assertThat(filmOptionalId1WithoutLikes)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getIdsOfLikers().size()).isEqualTo(0)
                );
    }
}
