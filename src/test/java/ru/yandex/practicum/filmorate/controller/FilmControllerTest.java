package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

public class FilmControllerTest {
    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private final UserStorage userStorage = new InMemoryUserStorage();

    @Test
    public void shouldCreateNewFilmAndGiveItBack() {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film with DiCaprio")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(195)
                .build();
        Assertions.assertDoesNotThrow(() -> filmStorage.createFilm(film));
        Assertions.assertNotNull(filmStorage.findAll());
    }

    @Test
    public void shouldThrowExceptionWithIncorrectName() {
        Film film = Film.builder()
                .name("")
                .description("Film with DiCaprio")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(195)
                .build();
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmStorage.createFilm(film));
        Assertions.assertEquals("Название не может быть пустым.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithIncorrectDescription() {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film with DiCaprio, but length of description is more than 200. aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(195)
                .build();
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmStorage.createFilm(film));
        Assertions.assertEquals("Максимальная длина описания — 200 символов.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithDescriptionLength200() {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film with DiCaprio, but length of description is 200. aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(195)
                .build();
        Assertions.assertEquals(200, film.getDescription().length());
        Assertions.assertDoesNotThrow(() -> filmStorage.createFilm(film));
    }

    @Test
    public void shouldThrowExceptionWithIncorrectReleaseDate() {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film with DiCaprio")
                .releaseDate(LocalDate.of(1885, 11, 01))
                .duration(195)
                .build();
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmStorage.createFilm(film));
        Assertions.assertEquals("Дата релиза — не раньше 28 декабря 1895 года.", exception.getMessage());
    }

    @Test
    public void shouldCreateNewFilmWithEarlierReleaseDate() {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film with DiCaprio")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(195)
                .build();
        Assertions.assertDoesNotThrow(() -> filmStorage.createFilm(film));
    }

    @Test
    public void shouldThrowExceptionWithZeroDuration() {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film with DiCaprio")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(0)
                .build();
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmStorage.createFilm(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithNegativeDuration() {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film with DiCaprio")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(-195)
                .build();
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmStorage.createFilm(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }

    @Test
    public void shouldCorrectlyUpdateFilm() {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film with DiCaprio")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(195)
                .build();
        Film defaultFilm = filmStorage.createFilm(film);
        Film filmWithUpdates = Film.builder()
                .id(1)
                .name("Titanic")
                .description("Film with DiCaprio.")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(195)
                .build();
        Assertions.assertDoesNotThrow(() -> filmStorage.updateFilm(filmWithUpdates));
        Assertions.assertEquals(filmWithUpdates, defaultFilm);
    }


    @Test
    public void shouldThrowExceptionWithNoId() {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film with DiCaprio")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(195)
                .build();
        Film defaultFilm = filmStorage.createFilm(film);
        Film filmWithUpdates = Film.builder()
                .name("Titanic")
                .description("Film with DiCaprio.")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(195)
                .build();
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmStorage.updateFilm(filmWithUpdates));
        Assertions.assertEquals("Обновлённые данные о фильме должны содержать положительный целочисленный Id.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithIncorrectId() {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film with DiCaprio")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(195)
                .build();
        Film defaultFilm = filmStorage.createFilm(film);
        Film filmWithUpdates = Film.builder()
                .id(-1)
                .name("Titanic")
                .description("Film with DiCaprio.")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(195)
                .build();
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmStorage.updateFilm(filmWithUpdates));
        Assertions.assertEquals("Обновлённые данные о фильме должны содержать положительный целочисленный Id.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithNotFoundId() {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film with DiCaprio")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(195)
                .build();
        Film defaultFilm = filmStorage.createFilm(film);
        Film filmWithUpdates = Film.builder()
                .id(3)
                .name("Titanic")
                .description("Film with DiCaprio.")
                .releaseDate(LocalDate.of(1997, 11, 01))
                .duration(195)
                .build();
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> filmStorage.updateFilm(filmWithUpdates));
        Assertions.assertEquals("Пост с id = " + filmWithUpdates.getId() + " не найден.", exception.getMessage());
    }
}
