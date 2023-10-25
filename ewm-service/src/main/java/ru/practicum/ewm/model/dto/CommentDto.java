package ru.practicum.ewm.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private UserShortDto author;
    private String text;
    private LocalDateTime timestamp;
}
