package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.Category;
import ru.practicum.model.User;
import ru.practicum.model.dto.CategoryDto;
import ru.practicum.model.dto.NewCategoryDto;
import ru.practicum.model.dto.NewUserRequest;
import ru.practicum.model.dto.UserDto;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.UserRepository;

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

    //GET /admin/users
    //Получение информации о пользователях
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

    //POST /admin/users
    //Добавление нового пользователя
    public UserDto createUser(NewUserRequest userRequest) {
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userRequest)));
    }

    //DELETE /admin/users/{userId}
    //Удаление пользователя
    public void deleteUser(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new NotFoundException("User " + userId + " not found.");
        }
        userRepository.deleteById(userId);
    }


    //POST /admin/categories
    //Добавление новой категории
    public CategoryDto createCategory(NewCategoryDto dto) {
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(dto)));
    }

    //PATCH /admin/categories/{catId}
    //Изменение категории
    public CategoryDto updateCategory(NewCategoryDto dto, Long catId) {
        if(!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Category " + catId + " not found.");
        }
        Category catToSave = categoryMapper.toCategory(dto);
        catToSave.setId(catId);
        return categoryMapper.toCategoryDto(categoryRepository.save(catToSave));
    }

    //DELETE /admin/categories/{catId}
    //Удаление категории
    public void deleteCategory(Long catId) {
        if(!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Category " + catId + " not found.");
        }
        categoryRepository.deleteById(catId);
    }
}
