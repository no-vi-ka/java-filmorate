package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class FilmTest {
    @Test
    public void usersShouldBeEqualsWhileDatasAreEqual() {
        Film film1 = new Film(1, "Titanic", "Film with DiCaprio", LocalDate.of(1997, 11, 01), 195);
        Film film2 = new Film(1, "Titanic", "Film with DiCaprio", LocalDate.of(1997, 11, 01), 195);
        Assertions.assertEquals(film1, film2);
    }
}
