package ru.yandex.praktikum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.praktikum.model.dto.CategoryDto;
import ru.yandex.praktikum.model.dto.EventShortDto;
import ru.yandex.praktikum.service.EwmServiceServerPublic;

import java.util.List;

@RestController
@AllArgsConstructor
public class EwmServiceControllerPublic {
    @Autowired
    private EwmServiceServerPublic servicePublic;

    @GetMapping("/categories")
    public List<CategoryDto> findAllCategories(@RequestParam(defaultValue = "0") Integer from, @RequestParam(defaultValue = "10") Integer size) {
        return servicePublic.findAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto findCategory(@PathVariable Long catId) {
        return servicePublic.findCategory(catId);
    }

    @GetMapping("/events")
    public List<EventShortDto> findEvents(@RequestParam String text, @RequestParam List<Integer> categories, @RequestParam Boolean paid, @RequestParam String rangeStart, @RequestParam String rangeEnd, @RequestParam Boolean onlyAvailable, @RequestParam String sort, @RequestParam Integer from, @RequestParam Integer size) {
        return servicePublic.findEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public EventShortDto findEventById(@PathVariable Long id) {
        return servicePublic.findEventById(id);
    }
}
