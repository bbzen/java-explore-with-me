package ru.yandex.praktikum.model.dto;

import java.time.LocalDateTime;

public interface DatedEvent {
    LocalDateTime getEventDate();
}
