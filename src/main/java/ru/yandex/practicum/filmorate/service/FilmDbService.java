package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPARating.MPARatingStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.filmLikes.FilmLikesStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmDbService {
    private static final LocalDate FIRST_FILM = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final MPARatingStorage mpaRatingStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final FilmLikesStorage filmLikesStorage;

    public List<Film> findAll() {
        List<Film> films = filmStorage.findAll();
//        Map<Long, Set<Genre>> mapFilmGenre = filmGenreStorage.findGenreOfFilm(films);
//        films.forEach(film -> Optional.ofNullable(mapFilmGenre.get(film.getId()))
//                .ifPresent(film::addGenre)
//        );
        log.info("Запрос на список всех фильмов выполнен.");
        return films;
    }

    public Film createFilm(Film film) {
        isValid(film);
        Set<Genre> genres = getGenresToFilm(film).stream()
                .sorted(Comparator.comparing(Genre::getId, Comparator.naturalOrder()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        MPARating mpa = getMPAtoFilm(film);
        Film newFilm = filmStorage.createFilm(film);
        filmGenreStorage.addGenreToFilm(newFilm, genres);
        newFilm.setMpa(mpa);
        newFilm.addGenre(genres);
        log.info("Фильм успешно добавлен: {}.", newFilm);
        return newFilm;
    }

    public Film updateFilm(Film film) {
        isValid(film);
        getFilmById(film.getId());
        filmGenreStorage.removeGenreFromFilm(film.getId());
        filmStorage.updateFilm(film);
        filmGenreStorage.addGenreToFilm(film, film.getGenres());
        log.info("Фильм успешно обновлен: {}.", film);
        return film;
    }

    public Film getFilmById(Long filmId) {
        Film findFilm = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id: " + filmId + " не найден."));
        findFilm.addGenre(filmGenreStorage.findGenreOfFilm(findFilm));
//                .getOrDefault(filmId, Set.of())
//        );
        log.info("Запрос по поиску фильма обработан. Найден фильм: {}.", findFilm);
        return findFilm;
    }

    public void addLike(Long filmId, Long userId) {
        filmLikesStorage.addLike(filmId, userId);
        log.info("Пользователь с id: {} поставил 'like' фильму с id: {}.", userId, filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        filmLikesStorage.deleteLike(filmId, userId);
        log.info("Пользователь с id: {} удалил 'like' у фильма с id: {}.", userId, filmId);
    }

    public List<Film> getMostLikedFilms(Integer count) {
        List<Film> films = filmStorage.getMostLikedFilms(count);
//        Map<Long, Set<Genre>> mapFilmsGenres = filmGenreStorage.findGenreOfFilm(films);
//        films.forEach(film -> {
//            Set<Genre> genres = mapFilmsGenres.getOrDefault(film.getId(), Set.of());
//            film.addGenre(genres);
//        });
        log.info("Запрос на получение популярных фильмов обработан.");
        return films;
    }

    private void isValid(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(FIRST_FILM)) {
            throw new ValidationException(String.format("Ошибка валидации, неверная дата релиза %s.", film.getName()));
        }
    }

    private Set<Genre> getGenresToFilm(Film film) {
        Set<Genre> genres = film.getGenres().stream()
                .map(genre -> genreStorage.getGenreById(genre.getId())
                        .orElseThrow(() -> new ValidationException("Указан несуществующий жанр.")))
                .collect(Collectors.toSet());
        film.addGenre(genres);
        return genres;
    }

    private MPARating getMPAtoFilm(Film film) {
        int mpaID = film.getMpa().getId();
        return mpaRatingStorage.getMPAById(mpaID)
                .orElseThrow(() -> new ValidationException("Указан несуществующий MPA рейтинг."));
    }

    public void deleteFilm(Long id) {
        filmStorage.deleteFilm(id);
        log.info("Фильм с id={} успешно удален.", id);
    }
}