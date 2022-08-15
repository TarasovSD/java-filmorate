package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface FriendsStorage {
    void addUserAsFriendUser(Long id, Long friendId);

    Set<User> getUserFriends(Long id);

    void removeUsersFromFriends(Long id, Long friendId);
}
