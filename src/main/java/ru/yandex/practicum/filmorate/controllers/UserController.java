package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping()
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user;
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void AddUserAsFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.AddUserAsFriendUser(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void DeleteUserFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.DeleteUserFromFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Set<User> getUserFriends(@PathVariable Long id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<User> getUserListOfMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getUserListOfMutualFriends(id, otherId);
    }

}
