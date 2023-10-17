package ru.yandex.praktikum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.praktikum.model.dto.CategoryDto;
import ru.yandex.praktikum.model.dto.EventFullDto;
import ru.yandex.praktikum.model.dto.EventShortDto;
import ru.yandex.praktikum.model.status.SortStatus;
import ru.yandex.praktikum.service.EwmServiceServerPublic;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class EwmServiceControllerPublic {
    @Autowired
    private EwmServiceServerPublic servicePublic;
    private final static String DATETIMEPATTERN = "yyyy-MM-dd HH:mm:ss";


    @GetMapping("/categories")
    public List<CategoryDto> findAllCategories(@RequestParam(defaultValue = "0") Integer from, @RequestParam(defaultValue = "10") Integer size) {
        return servicePublic.findAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto findCategory(@PathVariable Long catId) {
        return servicePublic.findCategory(catId);
    }

    @GetMapping("/events")
    public List<EventShortDto> findEvents(@RequestParam(defaultValue = "") String text,
                                          @RequestParam(required = false) List<Long> categories,
                                          @RequestParam(required = false) Boolean paid,
                                          @RequestParam @DateTimeFormat(pattern = DATETIMEPATTERN) LocalDateTime rangeStart,
                                          @RequestParam @DateTimeFormat(pattern = DATETIMEPATTERN) LocalDateTime rangeEnd,
                                          @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                          @RequestParam(defaultValue = "VIEWS") SortStatus sort,
                                          @Valid @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                          @Valid @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                          HttpServletRequest request) {
        return servicePublic.findEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/events/{id}")
    public EventFullDto findEventById(@PathVariable Long id, HttpServletRequest request) {
        return servicePublic.findEventById(id, request);
    }
}
