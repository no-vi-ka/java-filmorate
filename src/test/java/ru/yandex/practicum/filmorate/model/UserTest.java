package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class UserTest {
    @Test
    public void usersShouldBeEqualsWhileDatasAreEqual() {
        User user1 = new User(1, "no.vi.ka@yandex.ru", "no-vi-ka", "no.vi.ka", LocalDate.of(2000, 03, 10));
        User user2 = new User(1, "no.vi.ka@yandex.ru", "no-vi-ka", "no.vi.ka", LocalDate.of(2000, 03, 10));
        Assertions.assertEquals(user1, user2);
    }
}
