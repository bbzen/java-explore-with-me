package ru.yandex.praktikum.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotNull(message = "Invalid Compilation title.")
    @Size(min = 1, message = "Compilation title length must be more than 1 char.")
    @Size(max = 50, message = "Compilation title length must be less than 50 char.")
    private String title;
}
