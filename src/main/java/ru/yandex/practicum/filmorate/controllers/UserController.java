package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InputException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private Map<Long, User> users = new HashMap<>();

    private int generatorId = 1;


    @GetMapping("users")
    public List<User> getUsers() {
        List<User> usersList = new ArrayList<>();
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            usersList.add(entry.getValue());
        }
        return usersList;
    }

    @PostMapping("users")
    public User createUser(@Valid @RequestBody User user) {
        user.setId(generatorId);
        generatorId++;
        String name = user.getName();
        if (name.isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Текущее количество пользователей: {}", users.size());
        return user;
    }

    @PutMapping("users")
    public User updateUser(@Valid @RequestBody User user) {
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            if (user.getId() == entry.getKey()) {
                users.put(user.getId(), user);
                log.info("Пользователь с ID {} обновлен!", user.getId());
            } else {
                log.warn("Пользователь с ID {} не найден!", user.getId());
                throw new InputException("Такого пользователя не существует");
            }
        }
        return user;
    }
}
