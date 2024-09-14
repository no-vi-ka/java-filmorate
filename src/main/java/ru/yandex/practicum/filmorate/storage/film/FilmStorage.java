package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
public interface FilmStorage {
    List<Film> getAll();

    Film create(final Film film);

    Film update(final Film film);

    Optional<Film> getFilm(Long filmId);

    Film deleteFilm(Long id);

    List<Film> getAllPopularFilm(int count);
}