package ru.yandex.practicum.filmorate.storage.MPARating;

import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.Optional;
import java.util.List;

public interface MPARatingStorage {
    Optional<MPARating> getMPAById(int id);

    List<MPARating> getAllMPA();
}
