package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

public class UserControllerTest {
//    private final UserStorage userStorage = new InMemoryUserStorage();
//
//    @Test
//    public void shouldCreateNewUserAndGiveItBack() {
//        User user = User.builder()
//                .email("no.vi.ka@yandex.ru")
//                .login("no-vi-ka")
//                .name("no.vi.ka")
//                .birthday(LocalDate.of(2000, 03, 10))
//                .build();
//        Assertions.assertDoesNotThrow(() -> userStorage.createUser(user));
//        Assertions.assertNotNull(userStorage.findAll());
//    }
//
//    @Test
//    public void shouldSetNameFromLoginIfItIsEmpty() {
//        User user = User.builder()
//                .email("no.vi.ka@yandex.ru")
//                .login("no-vi-ka")
//                .name("")
//                .birthday(LocalDate.of(2000, 03, 10))
//                .build();
//        User createdUser = userStorage.createUser(user);
//        Assertions.assertEquals(createdUser.getLogin(), createdUser.getName());
//    }
//
//    @Test
//    public void shouldThrowExceptionWithEmailWithoutDogSymbol() {
//        User user = User.builder()
//                .email("no.vi.ka.yandex.ru")
//                .login("no-vi-ka")
//                .name("no.vi.ka")
//                .birthday(LocalDate.of(2000, 03, 10))
//                .build();
//        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> userStorage.createUser(user));
//        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWithEmptyEmail() {
//        User user = User.builder()
//                .email("")
//                .login("no-vi-ka")
//                .name("no.vi.ka")
//                .birthday(LocalDate.of(2000, 03, 10))
//                .build();
//        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> userStorage.createUser(user));
//        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWithEmptyLogin() {
//        User user = User.builder()
//                .email("no.vi.ka@yandex.ru")
//                .login("")
//                .name("no.vi.ka")
//                .birthday(LocalDate.of(2000, 03, 10))
//                .build();
//        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> userStorage.createUser(user));
//        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWithWhitespaceInLogin() {
//        User user = User.builder()
//                .email("no.vi.ka@yandex.ru")
//                .login("no vi ka")
//                .name("no.vi.ka")
//                .birthday(LocalDate.of(2000, 03, 10))
//                .build();
//        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> userStorage.createUser(user));
//        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWithBirthdayInFuture() {
//        User user = User.builder()
//                .email("no.vi.ka@yandex.ru")
//                .login("no-vi-ka")
//                .name("no.vi.ka")
//                .birthday(LocalDate.of(2100, 03, 10))
//                .build();
//        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> userStorage.createUser(user));
//        Assertions.assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
//    }
//
//    @Test
//    public void shouldCorrectlyUpdateUser() {
//        User user = User.builder()
//                .email("no.vi.ka@yandex.ru")
//                .login("no-vi-ka")
//                .name("no.vi.ka")
//                .birthday(LocalDate.of(2000, 03, 10))
//                .build();
//        User defaultUser = userStorage.createUser(user);
//        User userWithUpdates = User.builder()
//                .id(1)
//                .email("no.vi.ka@yandex.ru")
//                .login("no-vi-ka")
//                .name("no.vi.ka")
//                .birthday(LocalDate.of(2000, 03, 10))
//                .build();
//        Assertions.assertDoesNotThrow(() -> userStorage.updateUser(userWithUpdates));
//        Assertions.assertEquals(userWithUpdates, defaultUser);
//    }
//
//    @Test
//    public void shouldThrowExceptionWithNoId() {
//        User user = User.builder()
//                .email("no.vi.ka@yandex.ru")
//                .login("no-vi-ka")
//                .name("no.vi.ka")
//                .birthday(LocalDate.of(2000, 03, 10))
//                .build();
//        User defaultUser = userStorage.createUser(user);
//        User userWithUpdates = User.builder()
//                .email("no.vi.ka@yandex.ru")
//                .login("no-vi-ka")
//                .name("no.vi.ka")
//                .birthday(LocalDate.of(2000, 03, 10))
//                .build();
//        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> userStorage.updateUser(userWithUpdates));
//        Assertions.assertEquals("Обновлённые данные о пользователе должны содержать положительный целочисленный Id.", exception.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWithIncorrectId() {
//        User user = User.builder()
//                .email("no.vi.ka@yandex.ru")
//                .login("no-vi-ka")
//                .name("no.vi.ka")
//                .birthday(LocalDate.of(2000, 03, 10))
//                .build();
//        User defaultUser = userStorage.createUser(user);
//        User userWithUpdates = User.builder()
//                .id(-1)
//                .email("no.vi.ka@yandex.ru")
//                .login("no-vi-ka")
//                .name("no.vi.ka")
//                .birthday(LocalDate.of(2000, 03, 10))
//                .build();
//        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> userStorage.updateUser(userWithUpdates));
//        Assertions.assertEquals("Обновлённые данные о пользователе должны содержать положительный целочисленный Id.", exception.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWithNotFoundId() {
//        User user = User.builder()
//                .email("no.vi.ka@yandex.ru")
//                .login("no-vi-ka")
//                .name("no.vi.ka")
//                .birthday(LocalDate.of(2000, 03, 10))
//                .build();
//        User defaultUser = userStorage.createUser(user);
//        User userWithUpdates = User.builder()
//                .id(3)
//                .email("no.vi.ka@yandex.ru")
//                .login("no-vi-ka")
//                .name("no.vi.ka")
//                .birthday(LocalDate.of(2000, 03, 10))
//                .build();
//        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> userStorage.updateUser(userWithUpdates));
//        Assertions.assertEquals("Пользователь с id = " + userWithUpdates.getId() + " не найден.", exception.getMessage());
//    }
}
