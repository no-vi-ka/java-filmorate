package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder(toBuilder = true)
public class Genre {
    @Positive
    private final int id;
    @NotBlank
    private String name;
}

