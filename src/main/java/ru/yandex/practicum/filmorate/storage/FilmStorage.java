package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Collection<Film> findAll();

    public Film getFilmById(Long id);

    public Film createFilm(Film newFilm);

    public Film updateFilm(Film filmToUpdate);

    public void addFilmToMap(Film film);

    public void checkFilm(Film filmToCheck);
}
