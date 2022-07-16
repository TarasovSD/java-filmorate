package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserStorage inMemoryUserStorage;

    public List<User> getUsers() {
            return inMemoryUserStorage.getUsers();
    }

    public User getUserById(Long id) {
        User user = inMemoryUserStorage.getUserById(id);
        return user;
    }

    public User createUser(User user) {
        return inMemoryUserStorage.createUser(user);
    }


    public User updateUser(User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    public void AddUserAsFriendUser(Long id, Long friendId) {
        User userToAddAsFriend = inMemoryUserStorage.getUserById(id);
        User userForAddAsFriend = inMemoryUserStorage.getUserById(friendId);
        Set<User> friendsUserToAddAsFriend = userToAddAsFriend.getFriends();
        friendsUserToAddAsFriend.add(userForAddAsFriend);
        userToAddAsFriend.setFriends(friendsUserToAddAsFriend);
        Set<User> friendsUserForAddAsFriend = userForAddAsFriend.getFriends();
        friendsUserForAddAsFriend.add(userToAddAsFriend);
        userForAddAsFriend.setFriends(friendsUserForAddAsFriend);
        log.info("Пользователи с ID {} и {} добавлены друг к другу в список друзей!", id, friendId);
    }

    public void DeleteUserFromFriends(Long id, Long friendId) {
        User userOne = inMemoryUserStorage.getUserById(id);
        User userTwo = inMemoryUserStorage.getUserById(friendId);
        checkUsers(friendId, id, userTwo, userOne);
        checkUsers(id, friendId, userOne, userTwo);
    }



    public Set<User> getUserFriends(Long id) {
        return inMemoryUserStorage.getUserById(id).getFriends();
    }

    public Set<User> getUserListOfMutualFriends(Long id, Long otherId) {
        User userOne = inMemoryUserStorage.getUserById(id);
        User userTwo = inMemoryUserStorage.getUserById(otherId);
        Set<User> userOneFriends = userOne.getFriends();
        Set<User> userTwoFriends = userTwo.getFriends();
        Set<User> result = new HashSet<>();
        for (User user : userOneFriends) {
            if (userTwoFriends.contains(user)) {
                result.add(user);
            }
        }
        return result;
    }

    private void checkUsers(Long id, Long friendId, User userOne, User userTwo) {
        Set<User> userTwoFriends = userTwo.getFriends();
        User userToRemove = null;
        for (User user : userTwoFriends) {
            if (user.getId() == userOne.getId()) {
//                userTwoFriends.remove(userOne);
//                userTwo.setFriends(userTwoFriends);
                userToRemove = user;

            }
        }
        if (userToRemove != null) {
                userTwoFriends.remove(userToRemove);
                userTwo.setFriends(userTwoFriends);
        } else {
            log.warn("Пользователя с ID {} нет в списке друзей пользователя с ID {}", id, friendId);
            throw new NotFoundException("Такого пользователя нет в списке друзей!");
        }

//        for (int i = 0; i < userTwoFriends.size(); i++) {
//            User user = userTwoFriends.get(i);
//            if (user.getId() == userOne.getId()) {
//                userTwoFriends.remove(userOne);
//                userTwo.setFriends(userTwoFriends);
//            } else {
//                log.warn("Пользователя с ID {} нет в списке друзей пользователя с ID {}", id, friendId);
//                throw new NotFoundException("Такого пользователя нет в списке друзей!");
//            }
//        }
    }
}
