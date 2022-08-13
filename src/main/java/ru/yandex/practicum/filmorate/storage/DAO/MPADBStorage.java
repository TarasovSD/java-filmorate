package ru.yandex.practicum.filmorate.storage.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MPADBStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    public MPADBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> getMPAs() {
        String allMPAQuery = "select M.MPA_ID, M.MPA_NAME from MPA M";
        return jdbcTemplate.query(allMPAQuery, MPADBStorage::makeMPA);
    }

    @Override
    public MPA getMPAById(long id) {
        if (1 > id || id > 5) {
            throw new NotFoundException("Такого рейтинга MPA нет!");
        }
        String foundMPAQuery = "select M.MPA_ID, M.MPA_NAME from MPA M where MPA_ID = ?";
        if (jdbcTemplate.queryForObject(foundMPAQuery, MPADBStorage::makeMPA, id) != null) {
            return jdbcTemplate.queryForObject(foundMPAQuery, MPADBStorage::makeMPA, id);
        } else {
            throw new NotFoundException("Такого рейтинга MPA нет!");
        }
    }

    private static MPA makeMPA(ResultSet rs, int rowNum) throws SQLException {
        return new MPA(rs.getInt("MPA_ID"), rs.getString("MPA_NAME"));
    }
}