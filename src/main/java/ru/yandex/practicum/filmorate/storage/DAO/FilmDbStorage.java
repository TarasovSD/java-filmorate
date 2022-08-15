package ru.yandex.practicum.filmorate.storage.DAO;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;


    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID)" +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getCountOfLikes());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        return Optional.ofNullable(keyHolder.getKey())
                .map(Number::longValue)
                .map(id -> {
                    film.setId(id);
                    if (film.getGenres() != null) {
                        setGenres(film, keyHolder);
                    }
                    return film;
                }).orElse(null);
    }

    @Override
    public List<Film> getFilms() {
        String filmsTableQuery = "select " +
                "F.FILM_ID," +
                "F.FILM_NAME," +
                "F.DESCRIPTION," +
                "F.RELEASE_DATE," +
                "F.DURATION," +
                "F.RATE," +
                "F.MPA_ID," +
                "M.MPA_NAME " +
                "from FILMS F " +
                "inner join MPA M on M.MPA_ID = F.MPA_ID";
        List<Film> filmsFromFilmsTable = jdbcTemplate.query(filmsTableQuery, FilmDbStorage::makeFilm);
        for (Film film : filmsFromFilmsTable) {
            if (film != null) {
                String findGenresForFilmQuery = "select FG.GENRE_ID, G.GENRE_NAME from FILMS_TO_GENRES FG " +
                        "inner join GENRES G on FG.GENRE_ID = G.GENRE_ID where FG.FILM_ID = ?";
                List<Genre> foundGenresForFilm = jdbcTemplate.query(findGenresForFilmQuery,
                        new Object[]{film.getId()},
                        new int[]{Types.INTEGER},
                        (rs, rowNum) -> new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME")));
                film.setGenres(foundGenresForFilm);
            }
        }

        return filmsFromFilmsTable;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update FILMS " +
                "SET FILM_NAME = ?, " +
                "DESCRIPTION = ?," +
                "RELEASE_DATE = ?," +
                "DURATION = ?," +
                "RATE = ?," +
                "MPA_ID = ?" +
                "WHERE FILM_ID = ?";
        if (film.getId() >= 0) {
            jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getCountOfLikes(), film.getMpa().getId(),
                    film.getId());
        } else {
            throw new NotFoundException("Фильм с таким id не найден!");
        }
        if (film.getGenres() != null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            setGenres(film, keyHolder);
        }
        return film;
    }

    private Film setGenres(Film film, KeyHolder keyHolder) {
        if (film.getGenres().isEmpty()) {
            String filmDeleteGenresQuery = "delete from FILMS_TO_GENRES where FILM_ID = ?";
            jdbcTemplate.update(filmDeleteGenresQuery, film.getId());
            return film;
        }

        String filmDeleteGenresQuery = "delete from FILMS_TO_GENRES where FILM_ID = ?";
        jdbcTemplate.update(filmDeleteGenresQuery, film.getId());

        String sqlQueryForFilmsToGenres = "insert into FILMS_TO_GENRES (FILM_ID, GENRE_ID)" +
                "values (?, ?)";
        List<Genre> genresWithoutDuplicate = film.getGenres().stream().distinct().collect(Collectors.toList());
        for (Genre genre : genresWithoutDuplicate) {
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQueryForFilmsToGenres);
                stmt.setLong(1, film.getId());
                stmt.setLong(2, genre.getId());
                return stmt;
            }, keyHolder);
        }
        Film filmToSetGenres = film;
        filmToSetGenres.setGenres(genresWithoutDuplicate);
        return filmToSetGenres;
    }

    @Override
    public Film getFilmById(long filmId) {
        if (filmId <= 0) {
            throw new NotFoundException("Фильм с таким id не найден!");
        }
        String findFilmByIdQuery = "select " +
                "F.FILM_ID," +
                "F.FILM_NAME," +
                "F.DESCRIPTION," +
                "F.RELEASE_DATE," +
                "F.DURATION," +
                "F.RATE," +
                "F.MPA_ID," +
                "M.MPA_NAME from FILMS F " +
                "inner join MPA M on M.MPA_ID = F.MPA_ID " +
                "where F.FILM_ID = ?";
        Film foundFilm = jdbcTemplate.queryForObject(findFilmByIdQuery, FilmDbStorage::makeFilm, filmId);
        if (foundFilm != null) {
            String findGenresForFilmQuery = "select FG.GENRE_ID, G.GENRE_NAME from FILMS_TO_GENRES FG " +
                    "inner join GENRES G on FG.GENRE_ID = G.GENRE_ID where FG.FILM_ID = ?";
            List<Genre> foundGenresForFilm = jdbcTemplate.query(findGenresForFilmQuery,
                    new Object[]{filmId},
                    new int[]{Types.INTEGER},
                    (rs, rowNum) -> new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME")));
            foundFilm.setGenres(foundGenresForFilm);
            String findLikesForFilmQuery = "select FU.USER_ID from FILMS_TO_USERS FU " +
                    "where FU.FILM_ID = ?";
            List<Long> foundLikesForFilm = jdbcTemplate.query(findLikesForFilmQuery,
                    new Object[]{filmId},
                    new int[]{Types.INTEGER},
                    (rs, rowNum) -> rs.getLong("USER_ID"));
            Set<Long> foundLikesForFilmSet = new HashSet<>(foundLikesForFilm);
            foundFilm.setIdsOfLikers(foundLikesForFilmSet);
        }
        return foundFilm;
    }

    private static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        List<Genre> listOfGenres = new ArrayList<>();
        return Film.builder()
                .id(rs.getLong("FILM_ID"))
                .name(rs.getString("FILM_NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .countOfLikes(rs.getInt("RATE"))
                .mpa(new MPA(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")))
                .genres(listOfGenres)
                .build();
    }
}
