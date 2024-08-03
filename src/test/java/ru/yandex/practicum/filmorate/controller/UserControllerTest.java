package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTest {
    UserController controller = new UserController();

    @Test
    public void shouldCreateNewUserAndGiveItBack() {
        User user = new User("no.vi.ka@yandex.ru", "no-vi-ka", "no.vi.ka", LocalDate.of(2000, 03, 10));
        Assertions.assertDoesNotThrow(() -> controller.createUser(user));
        Assertions.assertNotNull(controller.findAll());
    }

    @Test
    public void shouldSetNameFromLoginIfItIsEmpty() {
        User user = new User("no.vi.ka@yandex.ru", "no-vi-ka", "", LocalDate.of(2000, 03, 10));
        User createdUser = controller.createUser(user);
        Assertions.assertEquals(createdUser.getLogin(), createdUser.getName());
    }

    @Test
    public void shouldThrowExceptionWithEmailWithoutDogSymbol() {
        User user = new User("no.vi.ka.yandex.ru", "no-vi-ka", "no.vi.ka", LocalDate.of(2000, 03, 10));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.createUser(user));
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithEmptyEmail() {
        User user = new User("", "no-vi-ka", "no.vi.ka", LocalDate.of(2000, 03, 10));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.createUser(user));
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithEmptyLogin() {
        User user = new User("no.vi.ka@yandex.ru", "", "no.vi.ka", LocalDate.of(2000, 03, 10));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.createUser(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithWhitespaceInLogin() {
        User user = new User("no.vi.ka@yandex.ru", "no vi ka", "no.vi.ka", LocalDate.of(2000, 03, 10));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.createUser(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithBirthdayInFuture() {
        User user = new User("no.vi.ka@yandex.ru", "no-vi-ka", "no.vi.ka", LocalDate.of(2100, 03, 10));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.createUser(user));
        Assertions.assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    public void shouldCorrectlyUpdateUser() {
        User user = new User("no.vi.ka@yandex.ru", "no-vi-ka", "no.vi.ka", LocalDate.of(2000, 03, 10));
        User defaultUser = controller.createUser(user);
        User userWithUpdates = new User(1, "no.vi.ka.2000@yandex.ru", "no-vi-ka", "no.vi.ka", LocalDate.of(2000, 03, 10));
        Assertions.assertDoesNotThrow(() -> controller.updateUser(userWithUpdates));
        Assertions.assertEquals(userWithUpdates, defaultUser);
    }

    @Test
    public void shouldThrowExceptionWithNoId() {
        User user = new User("no.vi.ka@yandex.ru", "no-vi-ka", "no.vi.ka", LocalDate.of(2000, 03, 10));
        User defaultUser = controller.createUser(user);
        User userWithUpdates = new User("no.vi.ka.2000@yandex.ru", "no-vi-ka", "no.vi.ka", LocalDate.of(2000, 03, 10));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.updateUser(userWithUpdates));
        Assertions.assertEquals("Обновлённые данные о пользователе должны содержать положительный целочисленный Id.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithIncorrectId() {
        User user = new User("no.vi.ka@yandex.ru", "no-vi-ka", "no.vi.ka", LocalDate.of(2000, 03, 10));
        User defaultUser = controller.createUser(user);
        User userWithUpdates = new User(-1, "no.vi.ka.2000@yandex.ru", "no-vi-ka", "no.vi.ka", LocalDate.of(2000, 03, 10));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.updateUser(userWithUpdates));
        Assertions.assertEquals("Обновлённые данные о пользователе должны содержать положительный целочисленный Id.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithNotFoundId() {
        User user = new User("no.vi.ka@yandex.ru", "no-vi-ka", "no.vi.ka", LocalDate.of(2000, 03, 10));
        User defaultUser = controller.createUser(user);
        User userWithUpdates = new User(3, "no.vi.ka.2000@yandex.ru", "no-vi-ka", "no.vi.ka", LocalDate.of(2000, 03, 10));
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> controller.updateUser(userWithUpdates));
        Assertions.assertEquals("Пользователь с id = " + userWithUpdates.getId() + " не найден.", exception.getMessage());
    }
}
