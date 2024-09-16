package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({UserDbStorage.class, UserMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserStorageTest {
    private static final int COUNT_USERS_IN_DB = 2;
    private static final Long TEST_USER_ID = Long.valueOf(1);
    private static final Long TEST_USER_ID2 = Long.valueOf(2);
    private final UserDbStorage userDbStorage;


    @Test
    @DisplayName("Созданный пользователь равен полученному по id.")
    public void findUserByIdAndCreateTest() {
        User user = User.builder()
                .id(TEST_USER_ID)
                .name("test")
                .login("test")
                .email("test@test.test")
                .birthday(LocalDate.of(2000, 11, 11)).build();
        User created = userDbStorage.createUser(user);
        Optional<User> userFrom = userDbStorage.getUserById(TEST_USER_ID);
        assertThat(userFrom)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(created);
    }

    @Test
    @DisplayName("Должен возвращать всех пользователей")
    public void getAllUsersTest() {
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
        assertThat(userDbStorage.findAll().size()).isEqualTo(COUNT_USERS_IN_DB);
    }

    @Test
    @DisplayName("Обновлённый пользователь равен полученному из базы.")
    public void updateUsersTest() {
        User user = User.builder()
                .id(TEST_USER_ID)
                .name("test")
                .login("test")
                .email("test@test.test")
                .birthday(LocalDate.of(2000, 11, 11)).build();
        User created = userDbStorage.createUser(user);
        User userToUpdate = User.builder()
                .id(TEST_USER_ID)
                .name("test4")
                .login("test4")
                .email("test4@test.test")
                .birthday(LocalDate.of(2004, 11, 11)).build();
        User updated = userDbStorage.updateUser(userToUpdate);
        User fromDb = userDbStorage.getUserById(TEST_USER_ID).get();
        assertThat(updated).usingRecursiveComparison().isEqualTo(fromDb);
    }
}