package ru.yandex.practicum.filmorate;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserPutException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    List<User> users1=new ArrayList<>();

    private int generatorId = 1;


    @GetMapping("users")
    public HashMap<Integer, User> getUser() {
        return users;
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
        return user;
    }

    @PutMapping("users")
    public User updateUser(@Valid @RequestBody User user) {
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            if (user.getId() == entry.getKey()) {
                users.put(user.getId(), user);
            } else {
                throw new UserPutException("Такого пользователя не существует");
            }
        }
        return user;
    }
}
