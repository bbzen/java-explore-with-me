package ru.yandex.praktikum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.mapper.CategoryMapper;
import ru.yandex.praktikum.mapper.UserMapper;
import ru.yandex.praktikum.model.CategoryDto;
import ru.yandex.praktikum.model.NewCategoryDto;
import ru.yandex.praktikum.model.NewUserRequest;
import ru.yandex.praktikum.model.UserDto;
import ru.yandex.praktikum.repository.CategoryRepository;
import ru.yandex.praktikum.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class EwmServiceServerAdmin {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private final CategoryMapper categoryMapper;

    public UserDto createUser(NewUserRequest userRequest) {
        return userRepository.save(userMapper.toUserDto(userRequest));
    }

    public List<UserDto> findUsers(List<Long> ids, Integer from, Integer size) {
        if (ids != null && !ids.isEmpty()) {
            return userRepository.findAllById(ids);
        } else {
            Pageable page = PageRequest.of(Math.abs(from / size), size);
            return userRepository.findAll(page).toList();
        }
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public CategoryDto createCategory(NewCategoryDto dto) {
        return categoryRepository.save(categoryMapper.toCategoryDto(dto));
    }

    public CategoryDto updateCategory(NewCategoryDto dto, Long catId) {
        categoryRepository.updateById(dto.getName(), catId);
        return new CategoryDto(catId, dto.getName());
    }

    public void deleteCategory(Long catId) {
        categoryRepository.deleteById(catId);
    }
}
