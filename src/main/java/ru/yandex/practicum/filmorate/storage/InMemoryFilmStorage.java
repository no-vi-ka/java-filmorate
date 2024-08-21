package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();
    private long idGenerator = 1;
    private final LocalDate theEarliestFilmDate = LocalDate.of(1895, 12, 28);


    @Override
    public Collection<Film> findAll() {
        log.info("Обрабатывается GET запрос от клиента.");
        return films.values();
    }

    @Override
    public Film getFilmById(Long id) {
        return films.get(id);
    }

    @Override
    @PostMapping
    public Film createFilm(@RequestBody Film newFilm) {
        log.info("Обрабатывается POST запрос от клиента.");
        checkFilm(newFilm);
        log.info("Данные о фильме (название, описание, дата выхода, продолжительность) корректны.");
        newFilm.setId(idGenerator++);
        films.put(newFilm.getId(), newFilm);
        log.info("Информация о фильме успешно сохранена и добавлена к списку фильмов.");
        return newFilm;
    }

    @Override
    @PutMapping
    public Film updateFilm(@RequestBody Film filmToUpdate) {
        log.info("Обрабатывается PUT запрос от клиента.");
        checkFilm(filmToUpdate);
        log.info("Данные о фильме (название, описание, дата выхода, продолжительность) корректны.");
        if (filmToUpdate.getId() <= 0) {
            log.error("Ошибка корректности id.");
            throw new ValidationException("Обновлённые данные о фильме должны содержать положительный целочисленный Id.");
        }
        if (!films.containsKey(filmToUpdate.getId())) {
            log.error("Указан несуществующий id.");
            throw new NotFoundException("Пост с id = " + filmToUpdate.getId() + " не найден.");
        }
        Film filmFromMap = films.get(filmToUpdate.getId());
        filmFromMap.setName(filmToUpdate.getName());
        filmFromMap.setDescription(filmToUpdate.getDescription());
        filmFromMap.setReleaseDate(filmToUpdate.getReleaseDate());
        filmFromMap.setDuration(filmToUpdate.getDuration());
        filmFromMap.setLikeCounts(filmToUpdate.getLikeCounts());
        films.put(filmFromMap.getId(), filmFromMap);
        log.info("Данные о фильме успешно обновлены.");
        return filmFromMap;
    }

    @Override
    public void addFilmToMap(Film film) {
        films.put(film.getId(), film);
    }

    public void checkFilm(Film filmToCheck) {
        if (filmToCheck.getName() == null || filmToCheck.getName().isBlank()) {
            log.error("Ошибка: введено некорректное название фильма.");
            throw new ValidationException("Название не может быть пустым.");
        }
        if (filmToCheck.getDescription().length() > 200) {
            log.error("Ошибка: введено описание фильма некорректной длины.");
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (filmToCheck.getReleaseDate().isBefore(theEarliestFilmDate)) {
            log.error("Ошибка: введена слишком ранняя дата выхода фильма.");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }
        if (filmToCheck.getDuration() <= 0) {
            log.error("Ошибка: введена некорректная продолжительность фильма.");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }
}
