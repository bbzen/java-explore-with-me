package ru.yandex.praktikum.model.dto;

import lombok.Data;
import ru.yandex.praktikum.model.status.ParticipationRequestStatus;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private ParticipationRequestStatus status;
}
