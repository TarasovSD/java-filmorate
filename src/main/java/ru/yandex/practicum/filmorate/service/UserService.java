package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DAO.FriendsDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    private final FriendsDbStorage friendsDbStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendsDbStorage friendsDbStorage) {
        this.userStorage = userStorage;
        this.friendsDbStorage = friendsDbStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }


    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void AddUserAsFriendUser(Long id, Long friendId) {
        friendsDbStorage.addUserAsFriendUser(id, friendId);
    }

    public void DeleteUserFromFriends(Long id, Long friendId) {
        friendsDbStorage.removeUsersFromFriends(id, friendId);
    }


    public Set<User> getUserFriends(Long id) {
        return friendsDbStorage.getUserFriends(id);
    }

    public Set<User> getUserListOfMutualFriends(Long id, Long otherId) {
        Set<User> userOneFriends = getUserFriends(id);
        Set<User> userTwoFriends = getUserFriends(otherId);
        Set<User> result = new HashSet<>();
        for (User user : userOneFriends) {
            if (userTwoFriends.contains(user)) {
                result.add(user);
            }
        }
        return result;
    }
}
