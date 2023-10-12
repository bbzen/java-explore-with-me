package ru.yandex.praktikum.model.dto;

import lombok.Data;
import ru.yandex.praktikum.model.status.ParticipationRequestStatus;

@Data
public class ParticipantRequestDto {
    private String created;
    private Long event;
    private Long id;
    private Long requester;
    private ParticipationRequestStatus status;
}
