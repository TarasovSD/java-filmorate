package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();

    private int generatorId = 1;

    @Override
    public List<User> getUsers() {
        List<User> usersList = new ArrayList<>();
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            usersList.add(entry.getValue());
        }
        return usersList;
    }

    @Override
    public User createUser(User user) {
        user.setId(generatorId);
        generatorId++;
        String name = user.getName();
        if (name.isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь с id {} создан!", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            if (user.getId() == entry.getKey()) {
                users.put(user.getId(), user);
                log.info("Пользователь с ID {} обновлен!", user.getId());
            } else {
                log.warn("Пользователь с ID {} не найден!", user.getId());
                throw new NotFoundException("Такого пользователя не существует");
            }
        }
        return user;
    }

    @Override
    public User getUserById(Long id) {
//        User user = null;
//        for (Long userId : users.keySet()) {
//            if (userId == id) {
//                user = users.get(id);
//            }
//        }
//        if (user != null) {
//            return user;
//        } else {
//            log.warn("Пользователь с ID {} не найден!", id);
//            throw new NotFoundException("Такого пользователя не существует");
        User user = null;
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            if (id == entry.getKey()) {
                user = entry.getValue();
                log.info("Возвращен пользователь с ID {}!", user.getId());
            }
        }

        if (user != null) {
            return user;
        } else {
            log.warn("Пользователь с ID {} не найден!", id);
            throw new NotFoundException("Такого пользователя не существует");
        }
    }
}
