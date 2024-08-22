package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long filmId, long id) {
        if ((id <= 0) || (filmId <= 0)) {
            throw new ValidationException("The id cannot be negative.");
        }
        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден.");
        }
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
//        filmStorage.getFilmById(filmId).getLikeCounts().add(id);
//        userStorage.getUserById(id).getLikedFilmsId().add(filmId);


//        filmStorage.getFilmById(id).addLike(userId);
//        userStorage.getUserById(userId).addLikedFilm(id);


        Film film = filmStorage.getFilmById(filmId);
        filmStorage.checkFilm(film);
        User user = userStorage.getUserById(id);
        userStorage.checkUser(user);
        film.getLikeCounts().add(id);
        user.getLikedFilmsId().add(filmId);
        //user.addLikedFilm(id);
        if (filmId == 45) {
            throw new RuntimeException("45 id.");
        }
    }


    public void deleteLike(long id, long userId) {
        if ((userId <= 0) || (id <= 0)) {
            throw new ValidationException("The id cannot be negative.");
        }
        if (filmStorage.getFilmById(id) == null) {
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден.");
        }
        Film film = filmStorage.getFilmById(id);
        filmStorage.checkFilm(film);
        User user = userStorage.getUserById(userId);
        userStorage.checkUser(user);
        film.getLikeCounts().remove(userId);
        user.getLikedFilmsId().remove(id);
    }


    public Collection<Film> getMostLikedFilms(Integer count) {
        if (count <= 0) {
            throw new ValidationException("The count cannot be negative.");
        }
        List<Film> allFilmList = new ArrayList<>(filmStorage.findAll());
        List<Film> topNList = allFilmList.stream()
                .sorted((o1, o2) -> o2.getLikeCounts().size() - o1.getLikeCounts().size())
                .limit(count).collect(Collectors.toList());
        return topNList;
    }
}
