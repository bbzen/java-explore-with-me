package ru.yandex.praktikum.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompilationDto {
    private List<EventShortDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}