package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.model.dto.CategoryDto;
import ru.practicum.ewm.model.dto.CompilationDto;
import ru.practicum.ewm.model.dto.EventFullDto;
import ru.practicum.ewm.model.dto.EventShortDto;
import ru.practicum.ewm.model.status.SortStatus;
import ru.practicum.ewm.service.EwmServiceServerPublic;

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
    public List<CategoryDto> findAllCategories(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        return servicePublic.findAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto findCategory(@PathVariable Long catId) {
        return servicePublic.findCategory(catId);
    }

    @GetMapping("/events")
    public List<EventShortDto> findEvents(
            @RequestParam(defaultValue = "") String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATETIMEPATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATETIMEPATTERN) LocalDateTime rangeEnd,
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

    @GetMapping("/compilations")
    public List<CompilationDto> findAllCompilations(
            @RequestParam(required = false) Boolean pinned,
            @Valid @RequestParam(required = false, defaultValue = "0") @Min(0) int from,
            @Valid @RequestParam(required = false, defaultValue = "10") @Min(1) int size) {
        return servicePublic.findAllCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto findCompilationById(@PathVariable Long compId) {
        return servicePublic.findCompilationById(compId);
    }
}
