package ru.yandex.praktikum.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotNull(message = "Invalid Compilation title.")
    @Size(min = 1, message = "Compilation title length must be more than 1 char.")
    @Size(max = 50, message = "Compilation title length must be less than 50 char.")
    private String title;
}
