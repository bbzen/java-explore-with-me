package ru.yandex.praktikum.model.dto;

import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String email;
    private String name;
}
