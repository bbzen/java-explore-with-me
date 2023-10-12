package ru.yandex.praktikum.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewCategoryDto {
    @NotNull(message = "Invalid category name.")
    @Size(min = 1, message = "Category`s name length must be more than 1 char.")
    @Size(max = 50, message = "Category`s name length must be less than 50 char.")
    private String name;
}
