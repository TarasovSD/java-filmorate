package ru.yandex.practicum.filmorate.storage.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreDBStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        String allGenresQuery = "select G.GENRE_ID, G.GENRE_NAME from GENRES G";
        return jdbcTemplate.query(allGenresQuery, GenreDBStorage::makeGenre);
    }

    @Override
    public Genre getGenreById(long id) {
        if (1 > id || id > 6) {
            throw new NotFoundException("Такого рейтинга MPA нет!");
        }
        String foundGenreQuery = "select G.GENRE_ID, G.GENRE_NAME from GENRES G where GENRE_ID = ?";
        if (jdbcTemplate.queryForObject(foundGenreQuery, GenreDBStorage::makeGenre, id) != null) {
            return jdbcTemplate.queryForObject(foundGenreQuery, GenreDBStorage::makeGenre, id);
        } else {
            throw new NotFoundException("Такого рейтинга MPA нет!");
        }
    }

    private static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME"));
    }
}
