package ru.practicum.dto;

import lombok.Data;

@Data
public class StatInputDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
