package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class FilmTest {
    @Test
    public void filmsShouldBeEqualsWhileDatasAreEqual() {
        Film film1 = Film.builder()
                .name("Titanic")
                .description("Film with DiCaprio")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(195)
                .build();
        Film film2 = Film.builder()
                .name("Titanic")
                .description("Film with DiCaprio")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(195)
                .build();
        Assertions.assertEquals(film1, film2);
    }
}
