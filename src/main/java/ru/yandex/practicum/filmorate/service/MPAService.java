package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPARating.MPARatingStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MPAService {

    private final MPARatingStorage mpaRatingStorage;

    public MPARating getMPA(int id) {
        MPARating mpa = mpaRatingStorage.getMPAById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA с id: " + id + " не найден."));
        log.info("Запрос по поиску рейтинга MPA обработан. Найден рейтинг MPA: {}.", mpa);
        return mpa;
    }

    public List<MPARating> getAllMPA() {
        List<MPARating> mpaRatings = mpaRatingStorage.getAllMPA();
        log.info("Запрос на получение списка всех рейтингов MPA обработан. Найдены рейтинги MPA: {}", mpaRatings);
        return mpaRatings;
    }
}
