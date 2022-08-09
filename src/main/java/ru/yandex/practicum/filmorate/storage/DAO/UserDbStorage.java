package ru.yandex.practicum.filmorate.storage.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "select " +
                "USER_ID," +
                "USER_EMAIL," +
                "LOGIN," +
                "USER_NAME," +
                "BIRTHDAY from USERS";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "insert into USERS (USER_EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        return Optional.ofNullable(keyHolder.getKey())
                .map(Number::longValue)
                .map(id -> {
                    user.setId(id);
                    return user;
                }).orElse(null);
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update USERS " +
                "SET LOGIN = ?, " +
                "USER_NAME = ?," +
                "USER_EMAIL = ?," +
                "BIRTHDAY = ?" +
                "WHERE USER_ID = ?";
        if (user.getId() >= 0) {
            jdbcTemplate.update(sqlQuery, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        } else {
            throw new NotFoundException("Пользователь с таким id не найден!");
        }
        return user;
    }

    @Override
    public User getUserById(Long id) {
        String sqlQuery = "select " +
                "USER_ID," +
                "USER_EMAIL," +
                "LOGIN," +
                "USER_NAME," +
                "BIRTHDAY from USERS where USER_ID = ?";

        if (id >= 0) {
            return jdbcTemplate.queryForObject(sqlQuery, UserDbStorage::makeUser, id);
        } else {
            throw new NotFoundException("Пользователь с таким id не найден!");
        }
    }

    static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("USER_ID"))
                .email(rs.getString("USER_EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("USER_NAME"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}
