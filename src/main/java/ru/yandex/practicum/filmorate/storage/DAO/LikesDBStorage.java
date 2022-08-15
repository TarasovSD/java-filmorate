package ru.yandex.practicum.filmorate.storage.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class LikesDBStorage implements ru.yandex.practicum.filmorate.storage.LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikesDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Long> likeFilm(long id, long userId) {
        String sqlQueryForFilmsToGenres = "insert into FILMS_TO_USERS (FILM_ID, USER_ID)" +
                "values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQueryForFilmsToGenres);
            stmt.setLong(1, id);
            stmt.setLong(2, userId);
            return stmt;
        }, keyHolder);

        String findLikesForFilmQuery = "select FU.USER_ID from FILMS_TO_USERS FU " +
                "where FU.FILM_ID = ?";

        List<Long> foundLikesForFilm = jdbcTemplate.query(findLikesForFilmQuery,
                new Object[]{userId},
                new int[]{Types.INTEGER},
                (rs, rowNum) -> rs.getLong("USER_ID"));
        Set<Long> idsOfLikers = new HashSet<>(foundLikesForFilm);
        return idsOfLikers;
    }

    @Override
    public Set<Long> deleteLikeFilm(long id, long userId) {
        String deleteLikesForFilmQuery = "delete from FILMS_TO_USERS where USER_ID = ? and FILM_ID = ?";
        jdbcTemplate.update(deleteLikesForFilmQuery, userId, id);

        String findLikesForFilmQuery = "select FU.USER_ID from FILMS_TO_USERS FU " +
                "where FU.FILM_ID = ?";
        List<Long> foundLikesForFilm = jdbcTemplate.query(findLikesForFilmQuery,
                new Object[]{userId},
                new int[]{Types.INTEGER},
                (rs, rowNum) -> rs.getLong("USER_ID"));
        Set<Long> idsOfLikers = new HashSet<>(foundLikesForFilm);
        return idsOfLikers;
    }
}