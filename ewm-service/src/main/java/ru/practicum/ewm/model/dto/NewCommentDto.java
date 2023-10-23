package ru.practicum.ewm.model.dto;

import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;

import java.time.LocalDateTime;

public class NewCommentDto {
    private User author;
    private Event event;
    private String text;
    private LocalDateTime timestamp;
}

