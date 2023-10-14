package ru.yandex.praktikum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.model.dto.CategoryDto;
import ru.yandex.praktikum.model.dto.NewCategoryDto;
import ru.yandex.praktikum.model.dto.NewUserRequest;
import ru.yandex.praktikum.model.dto.UserDto;
import ru.yandex.praktikum.service.EwmServiceServerAdmin;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class EwmServiceControllerAdmin {
    @Autowired
    private EwmServiceServerAdmin serviceServerAdmin;

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam List<Long> ids, @RequestParam(required = false, defaultValue = "0") Integer from, @RequestParam(required = false, defaultValue = "10") Integer size) {
        return serviceServerAdmin.findUsers(ids, from, size);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@RequestBody NewCategoryDto dto, @PathVariable Long catId) {
        return serviceServerAdmin.updateCategory(dto, catId);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest userRequest) {
        return serviceServerAdmin.createUser(userRequest);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        return serviceServerAdmin.createCategory(categoryDto);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        serviceServerAdmin.deleteUser(userId);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        serviceServerAdmin.deleteCategory(catId);
    }
}
