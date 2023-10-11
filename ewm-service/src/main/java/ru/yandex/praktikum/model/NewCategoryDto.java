package ru.yandex.praktikum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotNull(message = "Invalid category name.")
    @Size(min = 1, message = "Category`s name length must be more than 1 char.")
    @Size(max = 50, message = "Category`s name length must be less than 50 char.")
    private String name;
}
