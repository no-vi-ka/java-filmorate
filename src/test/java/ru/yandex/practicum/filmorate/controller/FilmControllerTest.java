package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmControllerTest {

    FilmController controller = new FilmController();

    @Test
    public void shouldCreateNewFilmAndGiveItBack() {
        Film film = new Film("Titanic", "Film with DiCaprio", LocalDate.of(1997, 11, 01), 195);
        Assertions.assertDoesNotThrow(() -> controller.createFilm(film));
        Assertions.assertNotNull(controller.findAll());
    }

    @Test
    public void shouldThrowExceptionWithIncorrectName() {
        Film film = new Film("", "Film with DiCaprio", LocalDate.of(1997, 11, 01), 195);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
        Assertions.assertEquals("Название не может быть пустым.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithIncorrectDescription() {
        Film film = new Film("Titanic", "Film with DiCaprio, but length of description is more than 200. aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", LocalDate.of(1997, 11, 01), 195);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
        Assertions.assertEquals("Максимальная длина описания — 200 символов.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithDescriptionLength200() {
        Film film = new Film("Titanic", "Film with DiCaprio, but length of description is 200. aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", LocalDate.of(1997, 11, 01), 195);
        Assertions.assertEquals(200, film.getDescription().length());
        Assertions.assertDoesNotThrow(() -> controller.createFilm(film));
    }

    @Test
    public void shouldThrowExceptionWithIncorrectReleaseDate() {
        Film film = new Film("Titanic", "Film with DiCaprio", LocalDate.of(1885, 11, 01), 195);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
        Assertions.assertEquals("Дата релиза — не раньше 28 декабря 1895 года.", exception.getMessage());
    }

    @Test
    public void shouldCreateNewFilmWithEarlierReleaseDate() {
        Film film = new Film("Titanic", "Film with DiCaprio", LocalDate.of(1895, 12, 28), 195);
        Assertions.assertDoesNotThrow(() -> controller.createFilm(film));
    }

    @Test
    public void shouldThrowExceptionWithZeroDuration() {
        Film film = new Film("Titanic", "Film with DiCaprio", LocalDate.of(1997, 11, 01), 0);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithNegativeDuration() {
        Film film = new Film("Titanic", "Film with DiCaprio", LocalDate.of(1997, 11, 01), -195);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }

    @Test
    public void shouldCorrectlyUpdateFilm() {
        Film film = new Film("Titanic", "Film with DiCaprio", LocalDate.of(1997, 11, 01), 195);
        Film defaultFilm = controller.createFilm(film);
        Film filmWithUpdates = new Film(1, "Titanic", "Film with DiCaprio.", LocalDate.of(1997, 11, 01), 195);
        Assertions.assertDoesNotThrow(() -> controller.updateFilm(filmWithUpdates));
        Assertions.assertEquals(filmWithUpdates, defaultFilm);
    }


    @Test
    public void shouldThrowExceptionWithNoId() {
        Film film = new Film("Titanic", "Film with DiCaprio", LocalDate.of(1997, 11, 01), 195);
        Film defaultFilm = controller.createFilm(film);
        Film filmWithUpdates = new Film("Titanic", "Film with DiCaprio.", LocalDate.of(1997, 11, 01), 195);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.updateFilm(filmWithUpdates));
        Assertions.assertEquals("Обновлённые данные о фильме должны содержать положительный целочисленный Id.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithIncorrectId() {
        Film film = new Film("Titanic", "Film with DiCaprio", LocalDate.of(1997, 11, 01), 195);
        Film defaultFilm = controller.createFilm(film);
        Film filmWithUpdates = new Film(-1, "Titanic", "Film with DiCaprio.", LocalDate.of(1997, 11, 01), 195);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> controller.updateFilm(filmWithUpdates));
        Assertions.assertEquals("Обновлённые данные о фильме должны содержать положительный целочисленный Id.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWithNotFoundId() {
        Film film = new Film("Titanic", "Film with DiCaprio", LocalDate.of(1997, 11, 01), 195);
        Film defaultFilm = controller.createFilm(film);
        Film filmWithUpdates = new Film(3, "Titanic", "Film with DiCaprio.", LocalDate.of(1997, 11, 01), 195);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> controller.updateFilm(filmWithUpdates));
        Assertions.assertEquals("Пост с id = " + filmWithUpdates.getId() + " не найден.", exception.getMessage());
    }
}
