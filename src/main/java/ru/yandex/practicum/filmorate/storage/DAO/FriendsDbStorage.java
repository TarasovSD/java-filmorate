package ru.yandex.practicum.filmorate.storage.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class FriendsDbStorage implements FriendsStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    UserStorage userStorage;

    @Autowired
    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUserAsFriendUser(Long id, Long friendId) {
        if (friendId <= 0) {
            throw new NotFoundException("Такого пользователя не существует!");
        }
        String sqlQuery = "insert into FRIEND_STATUS (USER_ID, FRIEND_ID) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            stmt.setLong(1, id);
            stmt.setLong(2, friendId);
            return stmt;
        }, keyHolder);
    }


    @Override
    public Set<User> getUserFriends(Long id) {
        String sqlQuery = "select FRIEND_ID from FRIEND_STATUS where USER_ID = ?";

        List<Map<String, Object>> friendsIds = jdbcTemplate.queryForList(sqlQuery, id);
        List<Long> friendsIdsAsLong = new ArrayList<>();
        for (Map<String, Object> friendsId : friendsIds) {
            for (Map.Entry<String, Object> entry : friendsId.entrySet()) {
                friendsIdsAsLong.add((Long) entry.getValue());
            }
        }
        Set<User> userFriends = new HashSet<>();
        for (Long userId : friendsIdsAsLong) {
            userFriends.add(userStorage.getUserById(userId));
        }
        return userFriends;
    }

    @Override
    public void removeUsersFromFriends(Long id, Long friendId) {
        String sqlQuery = "delete from FRIEND_STATUS where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
        jdbcTemplate.update(sqlQuery, friendId, id);
    }
}
