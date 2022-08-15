package ru.yandex.practicum.filmorate.storeges;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FriendStorageTest {

    private final UserStorage userStorage;

    private final FriendsStorage friendsStorage;

    @Test
    public void testFriendsStorage() {

        userStorage.createUser(new User(1l, "test@ya.ru", "testLogin", "Sergey",
                LocalDate.of(1990, 01, 01)));

        Optional<User> userOptionalUserId2 = Optional.ofNullable(userStorage.getUserById(1l));

        userStorage.createUser(new User(1l, "test2@ya.ru", "testLogin2", "Sergey2",
                LocalDate.of(1996, 01, 01)));

        friendsStorage.addUserAsFriendUser(1l, 2l);

        Optional<Set<User>> userId1Friends = Optional.ofNullable(friendsStorage.getUserFriends(1l));

        Optional<User> userId2 = Optional.ofNullable(userStorage.getUserById(2l));

        assertThat(userId1Friends)
                .isPresent()
                .hasValueSatisfying(userId1FriendsList ->
                        assertThat(userId1FriendsList.size()).isEqualTo(1)
                );

        assertThat(userId1Friends)
                .isPresent()
                .hasValueSatisfying(userId1FriendsList ->
                        assertThat(userId1FriendsList.contains(userId2.orElse(null))).isEqualTo(true)
                );

        friendsStorage.removeUsersFromFriends(1l, 2l);

        Optional<Set<User>> userId1FriendsWithoutFriend = Optional.ofNullable(friendsStorage.getUserFriends(1l));

        assertThat(userId1FriendsWithoutFriend)
                .isPresent()
                .hasValueSatisfying(userId1FriendsList ->
                        assertThat(userId1FriendsList.size()).isEqualTo(0)
                );
    }
}
