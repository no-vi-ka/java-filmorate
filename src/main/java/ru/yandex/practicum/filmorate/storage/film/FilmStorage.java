package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;
import java.util.List;

public interface FilmStorage {
    List<Film> findAll();

    Film createFilm(final Film film);

    Film updateFilm(final Film film);

    Optional<Film> getFilmById(Long filmId);

    void deleteFilm(Long id);

    List<Film> getMostLikedFilms(int count);
}