package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
//        User userToAddAsFriend = userStorage.getUserById(id);
//        User userForAddAsFriend = userStorage.getUserById(friendId);
//        Set<User> friendsUserToAddAsFriend = userToAddAsFriend.getFriends();
//        friendsUserToAddAsFriend.add(userForAddAsFriend);
//        userToAddAsFriend.setFriends(friendsUserToAddAsFriend);
//        Set<User> friendsUserForAddAsFriend = userForAddAsFriend.getFriends();
//        friendsUserForAddAsFriend.add(userToAddAsFriend);
//        userForAddAsFriend.setFriends(friendsUserForAddAsFriend);
//        log.info("Пользователи с ID {} и {} добавлены друг к другу в список друзей!", id, friendId);
    }

    public void DeleteUserFromFriends(Long id, Long friendId) {
//        User userOne = userStorage.getUserById(id);
//        User userTwo = userStorage.getUserById(friendId);
//        checkAndRemoveUsers(friendId, id, userTwo, userOne);
//        checkAndRemoveUsers(id, friendId, userOne, userTwo);
        friendsDbStorage.removeUsersFromFriends(id, friendId);
    }


    public Set<User> getUserFriends(Long id) {
        return friendsDbStorage.getUserFriends(id);
//        return userStorage.getUserById(id).getFriends();
    }

    public Set<User> getUserListOfMutualFriends(Long id, Long otherId) {
//        User userOne = userStorage.getUserById(id);
//        User userTwo = userStorage.getUserById(otherId);
//        Set<User> userOneFriends = userOne.getFriends();
//        Set<User> userTwoFriends = userTwo.getFriends();
//        Set<User> result = new HashSet<>();
//        for (User user : userOneFriends) {
//            if (userTwoFriends.contains(user)) {
//                result.add(user);
//            }
//        }
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

//    private void checkAndRemoveUsers(Long id, Long friendId, User userOne, User userTwo) {
//        Set<User> userTwoFriends = userTwo.getFriends();
//        User userToRemove = null;
//        for (User user : userTwoFriends) {
//            if (user.getId() == userOne.getId()) {
//                userToRemove = user;
//            }
//        }
//        if (userToRemove != null) {
//            userTwoFriends.remove(userToRemove);
//            userTwo.setFriends(userTwoFriends);
//        } else {
//            log.warn("Пользователя с ID {} нет в списке друзей пользователя с ID {}", id, friendId);
//            throw new NotFoundException("Такого пользователя нет в списке друзей!");
//        }
//    }
}
