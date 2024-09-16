package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({UserDbStorage.class, FriendshipDbStorage.class, UserMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FriendshipStorageTest {
    private static final Long TEST_USER_ID = Long.valueOf(1);
    private static final Long TEST_USER_ID2 = Long.valueOf(2);
    private final UserDbStorage userDbStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    @Test
    @DisplayName("Должен добавить пользователя в друзья")
    public void addFriendTest() {
        User user = User.builder()
                .id(TEST_USER_ID)
                .name("test")
                .login("test")
                .email("test@test.test")
                .birthday(LocalDate.of(2000, 11, 11)).build();
        User created1 = userDbStorage.createUser(user);
        User user2 = User.builder()
                .id(TEST_USER_ID2)
                .name("test2")
                .login("test2")
                .email("test2@test.test")
                .birthday(LocalDate.of(2000, 11, 11)).build();
        User created2 = userDbStorage.createUser(user2);
        friendshipDbStorage.addFriend(created1.getId(), created2.getId());
        assertThat(friendshipDbStorage.getUserFriends(created1.getId()).size()).isEqualTo(1);
        Assertions.assertTrue(friendshipDbStorage.getUserFriends(created1.getId()).contains(created2));
    }

    @Test
    @DisplayName("Должен удалить пользователя из друзей")
    public void removeFriendTest() {
        User user = User.builder()
                .id(TEST_USER_ID)
                .name("test")
                .login("test")
                .email("test@test.test")
                .birthday(LocalDate.of(2000, 11, 11)).build();
        User created1 = userDbStorage.createUser(user);
        User user2 = User.builder()
                .id(TEST_USER_ID2)
                .name("test2")
                .login("test2")
                .email("test2@test.test")
                .birthday(LocalDate.of(2000, 11, 11)).build();
        User created2 = userDbStorage.createUser(user2);
        friendshipDbStorage.addFriend(created1.getId(), created2.getId());
        friendshipDbStorage.deleteFriend(created1.getId(), created2.getId());
        assertThat(friendshipDbStorage.getUserFriends(created1.getId()).size()).isEqualTo(0);
    }
}