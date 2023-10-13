package ru.yandex.praktikum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.praktikum.model.dto.CategoryDto;
import ru.yandex.praktikum.service.EwmServiceServerPublic;

import java.util.List;

@RestController
@AllArgsConstructor
public class EwmServiceControllerPublic {
    @Autowired
    private final EwmServiceServerPublic servicePublic;

    @GetMapping("/categories")
    public List<CategoryDto> findAllCategories(@RequestParam(defaultValue = "0") Integer from, @RequestParam(defaultValue = "10") Integer size) {
        return servicePublic.findAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto findCategory(@PathVariable Long catId) {
        return servicePublic.findCategory(catId);
    }
}
