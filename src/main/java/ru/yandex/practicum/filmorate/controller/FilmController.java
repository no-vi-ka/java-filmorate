package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDbService;

import java.util.List;

    @RestController
    @RequestMapping("/films")
    @RequiredArgsConstructor
    public class FilmController {

        private final FilmDbService filmDbService;

        @GetMapping
        public List<Film> getAll() {
            List<Film> films = filmDbService.getAll();
            return films;
        }

        @PostMapping
        public Film create(@Valid @RequestBody final Film film) {
            return filmDbService.create(film);
        }

        @PutMapping
        public Film update(@Valid @RequestBody final Film film) {
            return filmDbService.update(film);
        }

        @GetMapping("{id}")
        public Film getFilmById(@PathVariable("id") Long filmId) {
            return filmDbService.getFilmById(filmId);
        }

        @DeleteMapping("/{id}")
        public Film deleteFilmByID(@PathVariable("id") Long filmId) {
            return filmDbService.deleteFilm(filmId);
        }

        @PutMapping("{id}/like/{userId}")
        public void putLikeFilm(@PathVariable("id") Long filmId,
                                @PathVariable("userId") Long userId) {
            filmDbService.addFilmLike(filmId, userId);
        }

        @DeleteMapping("{id}/like/{userId}")
        public void deleteFilmLike(@PathVariable("id") Long filmId,
                                   @PathVariable("userId") Long userId) {
            filmDbService.deleteFilmLike(filmId, userId);
        }

        @GetMapping("/popular")
        public List<Film> getPopularFilms(
                @RequestParam(defaultValue = "10") @Positive int count) {
            return filmDbService.getPopularFilms(count);
        }
    }