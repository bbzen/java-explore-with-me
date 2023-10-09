package ru.yandex.praktikum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantRequestDto {
    private String created;
    private Long event;
    private Long id;
    private Long requester;
    private ParticipationRequestStatus status;
}
