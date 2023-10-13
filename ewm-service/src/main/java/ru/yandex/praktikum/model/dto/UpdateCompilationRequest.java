package ru.yandex.praktikum.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCompilationRequest {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}