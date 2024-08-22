package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> findAll();

    Film getFilmById(Long id);

    Film createFilm(Film newFilm);

    Film updateFilm(Film filmToUpdate);

    void checkFilm(Film filmToCheck);
}
