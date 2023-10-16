package ru.yandex.praktikum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.praktikum.exception.BadRequestException;
import ru.yandex.praktikum.exception.NotFoundException;
import ru.yandex.praktikum.mapper.CategoryMapper;
import ru.yandex.praktikum.model.dto.CategoryDto;
import ru.yandex.praktikum.model.dto.EventShortDto;
import ru.yandex.praktikum.model.status.SortStatus;
import ru.yandex.praktikum.repository.CategoryRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EwmServiceServerPublic {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    //GET /categories
    //Получение категорий
    public List<CategoryDto> findAllCategories(Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new BadRequestException("From and size params cant be negative.");
        }
        Pageable page = PageRequest.of(Math.abs(from / size), size);
        return categoryRepository.findAll(page).toList().stream().map(categoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    //GET /categories/{catId}
    //Получение информации о категории по её идентификатору
    public CategoryDto findCategory(@PathVariable Long catId) {
        return categoryMapper.toCategoryDto(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("There is no such category with id " + catId)));
    }

    //GET /events
    //Получение событий с возможностью фильтрации
    public List<EventShortDto> findEvents(String text, List<Integer> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size) {
        LocalDateTime startLDT;
        LocalDateTime endLDT;
        if (!rangeStart.isBlank() && !rangeEnd.isBlank()) {
            String startDecoded = URLDecoder.decode(rangeStart, StandardCharsets.UTF_8);
            String endDecoded = URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8);
            startLDT = LocalDateTime.parse(startDecoded, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            endLDT = LocalDateTime.parse(endDecoded, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        Pageable page = PageRequest.of(Math.abs(from / size), size);
        if (SortStatus.EVENT_DATE.name().equalsIgnoreCase(sort)) {

        } else if (SortStatus.VIEWS.name().equalsIgnoreCase(sort)) {

        }
        return null;
    }

    //GET /events/{id}
    //Получение подробной информации об опубликованном событии по его идентификатору
    public EventShortDto findEventById(Long id) {
        return null;
    }
}


