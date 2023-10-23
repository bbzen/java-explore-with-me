package ru.practicum.ewm.model.dto;

import ru.practicum.ewm.model.User;

import java.time.LocalDateTime;

public class CommentDto {
    private User author;
    private String text;
    private LocalDateTime timestamp;
}
