package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

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
        User user = null;
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            if (Objects.equals(id, entry.getKey())) {
                user = entry.getValue();
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
