package ru.yandex.praktikum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.model.NewUserRequest;
import ru.yandex.praktikum.model.UserDto;
import ru.yandex.praktikum.service.EwmServiceServerAdmin;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class EwmServiceControllerAdmin {
    @Autowired
    private EwmServiceServerAdmin serviceServerAdmin;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody NewUserRequest userRequest) {
        return serviceServerAdmin.createUser(userRequest);
    }
}
