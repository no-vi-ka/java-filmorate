package ru.yandex.practicum.filmorate.storage.MPARating;

import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.List;
import java.util.Optional;

public interface MPARatingStorage {
    Optional<MPARating> getMPAById(int id);

    List<MPARating> getAllMPA();
}
