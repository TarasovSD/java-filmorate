package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
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
        if (film.getGenres() != null) {
            String sqlQueryForFilmsToGenres = "insert into FILMS_TO_GENRES (FILM_ID, GENRE_ID)" +
                    "values (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlQueryForFilmsToGenres);
                    stmt.setLong(1, film.getId());
                    stmt.setLong(2, genre.getId());
                    return stmt;
                }, keyHolder);
            }

        }
        return Optional.ofNullable(keyHolder.getKey())
                .map(Number::longValue)
                .map(id -> {
                    film.setId(id);
                    return film;
                }).orElse(null);
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "select " +
                "F.FILM_ID," +
                "F.FILM_NAME," +
                "F.DESCRIPTION," +
                "F.RELEASE_DATE," +
                "F.DURATION," +
                "F.RATE," +
                "F.MPA_ID," +
                "M.MPA_NAME," +
                "G.GENRE_NAME," +
                "FTG.GENRE_ID from FILMS F " +
                "inner join MPA M on M.MPA_ID = F.MPA_ID " +
                "inner join FILMS_TO_GENRES FTG on FTG.FILM_ID = F.FILM_ID " +
                "inner join GENRES G on FTG.GENRE_ID = G.GENRE_ID";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
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
        return film;
    }

    @Override
    public Film getFilmById(long filmId) {
        if (filmId <= 0) {
            throw new NotFoundException("Фильм с таким id не найден!");
        }
        String sqlQueryToFindGenres = "select " +
                "F.FILM_ID," +
                "G.GENRE_ID from FILMS F " +
                "inner join FILMS_TO_GENRES FTG on FTG.FILM_ID = F.FILM_ID " +
                "inner join GENRES G on FTG.GENRE_ID = G.GENRE_ID " +
                "where F.FILM_ID = ?";
        jdbcTemplate.queryForObject(sqlQueryToFindGenres, FilmDbStorage::makeGenre, filmId);
        String sqlQuery = "select " +
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
//        String sqlQuery = "select " +
//                "F.FILM_ID," +
//                "F.FILM_NAME," +
//                "F.DESCRIPTION," +
//                "F.RELEASE_DATE," +
//                "F.DURATION," +
//                "F.RATE," +
//                "F.MPA_ID," +
//                "M.MPA_NAME," +
//                "G.GENRE_NAME," +
//                "FTG.GENRE_ID from FILMS F " +
//                "inner join MPA M on M.MPA_ID = F.MPA_ID " +
//                "inner join FILMS_TO_GENRES FTG on FTG.FILM_ID = F.FILM_ID " +
//                "inner join GENRES G on FTG.GENRE_ID = G.GENRE_ID " +
//                "where F.FILM_ID = ?";
//        if (jdbcTemplate.queryForObject(sqlQuery, FilmDbStorage::makeFilm, filmId) != null) {
            return jdbcTemplate.queryForObject(sqlQuery, FilmDbStorage::makeFilm, filmId);
//        }


    }

    private static Genre makeGenre(ResultSet resultSet, int rowNum) {
        List <Genre> genres = new ArrayList<>();
        for (int i = 0; i <= rowNum; i++) {

           genres.add()
        }
        return Genre.b
    }

    private static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        List<Genre> listOfGenres = new ArrayList<>();
//        for (int i = 0; i <= rowNum; i++) {
//            listOfGenres.add(new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME")));
//        }
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
