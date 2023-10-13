package ru.yandex.praktikum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.mapper.CategoryMapper;
import ru.yandex.praktikum.mapper.UserMapper;
import ru.yandex.praktikum.model.Category;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.dto.CategoryDto;
import ru.yandex.praktikum.model.dto.NewCategoryDto;
import ru.yandex.praktikum.model.dto.NewUserRequest;
import ru.yandex.praktikum.model.dto.UserDto;
import ru.yandex.praktikum.repository.CategoryRepository;
import ru.yandex.praktikum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

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
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userRequest)));
    }

    public List<UserDto> findUsers(List<Long> ids, Integer from, Integer size) {
        List<User> result;
        if (ids != null && !ids.isEmpty()) {
            result = userRepository.findAllById(ids);
        } else {
            Pageable page = PageRequest.of(Math.abs(from / size), size);
            result =  userRepository.findAll(page).toList();
        }
        return result.stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public CategoryDto createCategory(NewCategoryDto dto) {
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(dto)));
    }

    public CategoryDto updateCategory(NewCategoryDto dto, Long catId) {
        Category catToSave = categoryMapper.toCategory(dto);
        catToSave.setId(catId);
        return categoryMapper.toCategoryDto(categoryRepository.save(catToSave));
    }

    public void deleteCategory(Long catId) {
        categoryRepository.deleteById(catId);
    }
}
