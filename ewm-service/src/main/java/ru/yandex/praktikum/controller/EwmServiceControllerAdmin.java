package ru.yandex.praktikum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.model.NewUserRequest;
import ru.yandex.praktikum.model.UserDto;
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

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid NewUserRequest userRequest) {
        return serviceServerAdmin.createUser(userRequest);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        serviceServerAdmin.deleteUser(userId);
    }
}
