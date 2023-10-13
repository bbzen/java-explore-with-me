package ru.yandex.praktikum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.praktikum.exception.InvalidParamsException;
import ru.yandex.praktikum.exception.NotFoundException;
import ru.yandex.praktikum.mapper.CategoryMapper;
import ru.yandex.praktikum.model.dto.CategoryDto;
import ru.yandex.praktikum.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EwmServiceServerPublic {
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final CategoryMapper categoryMapper;

    //GET /categories
    //Получение категорий
    public List<CategoryDto> findAllCategories(Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new InvalidParamsException("From and size params cant be negative.");
        }
        Pageable page = PageRequest.of(Math.abs(from / size), size);
        return categoryRepository.findAll(page).toList().stream().map(categoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    //GET /categories/{catId}
    //Получение информации о категории по её идентификатору
    public CategoryDto findCategory(@PathVariable Long catId) {
        return categoryMapper.toCategoryDto(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("There is no such category with id " + catId)));
    }
}
