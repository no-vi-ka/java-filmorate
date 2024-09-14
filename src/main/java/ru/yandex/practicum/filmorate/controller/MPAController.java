package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mpa")
public class MPAController {

    private final MPAService mpaService;

    @GetMapping("/{id}")
    public MPARating getMPAByID(@PathVariable int id) {
        return mpaService.getMPA(id);
    }

    @GetMapping
    public List<MPARating> getMPAs() {
        return mpaService.getAllMPA();
    }
}