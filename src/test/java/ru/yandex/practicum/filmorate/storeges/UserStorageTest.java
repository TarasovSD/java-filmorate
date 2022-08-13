package ru.yandex.practicum.filmorate.storeges;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {

    private final UserStorage userStorage;

    @Test
    public void testUserStorage() {

        userStorage.createUser(new User(1l, "test@ya.ru", "testLogin", "Sergey",
                LocalDate.of(1990, 01, 01)));

        Optional<User> userOptionalUserId2 = Optional.ofNullable(userStorage.getUserById(1l));

        assertThat(userOptionalUserId2)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1l)
                );

        assertThat(userOptionalUserId2)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "test@ya.ru")
                );

        assertThat(userOptionalUserId2)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "testLogin")
                );

        assertThat(userOptionalUserId2)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Sergey")
                );

        LocalDate testDate = LocalDate.of(1990,1,1);
        assertThat(userOptionalUserId2)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("birthday", testDate)
                );


        userStorage.updateUser(new User(1l, "testUpdate@ya.ru", "testLoginUpdate", "Vlad",
                LocalDate.of(1995, 1, 1)));

        Optional<User> userOptionalUpdateUser = Optional.ofNullable(userStorage.getUserById(1l));

        assertThat(userOptionalUpdateUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1l)
                );

        assertThat(userOptionalUpdateUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "testUpdate@ya.ru")
                );

        assertThat(userOptionalUpdateUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "testLoginUpdate")
                );

        assertThat(userOptionalUpdateUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Vlad")
                );

        LocalDate testDate1 = LocalDate.of(1995,1,1);
        assertThat(userOptionalUpdateUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("birthday", testDate1)
                );

        userStorage.createUser(new User(1l, "test2@ya.ru", "testLogin2", "Sergey2",
                LocalDate.of(1996, 01, 01)));

        Optional<List<User>> listOfUsersOptional = Optional.ofNullable(userStorage.getUsers());

        assertThat(listOfUsersOptional)
                .isPresent()
                .hasValueSatisfying(listUsers ->
                        assertThat(listUsers).isNotNull()
                );

        assertThat(listOfUsersOptional)
                .isPresent()
                .hasValueSatisfying(listUsers ->
                        assertThat(listUsers.size()).isEqualTo(2)
                );

        assertThat(listOfUsersOptional)
                .isPresent()
                .hasValueSatisfying(listUsers ->
                        assertThat(listUsers.get(1)).hasFieldOrPropertyWithValue("id", 2l)
                );

        assertThat(listOfUsersOptional)
                .isPresent()
                .hasValueSatisfying(listUsers ->
                        assertThat(listUsers.get(1)).hasFieldOrPropertyWithValue("email", "test2@ya.ru")
                );

        assertThat(listOfUsersOptional)
                .isPresent()
                .hasValueSatisfying(listUsers ->
                        assertThat(listUsers.get(1)).hasFieldOrPropertyWithValue("login", "testLogin2")
                );

        assertThat(listOfUsersOptional)
                .isPresent()
                .hasValueSatisfying(listUsers ->
                        assertThat(listUsers.get(1)).hasFieldOrPropertyWithValue("name", "Sergey2")
                );

        LocalDate testDate2 = LocalDate.of(1996,1,1);
        assertThat(listOfUsersOptional)
                .isPresent()
                .hasValueSatisfying(listUsers ->
                        assertThat(listUsers.get(1)).hasFieldOrPropertyWithValue("birthday", testDate2)
                );
    }
}
