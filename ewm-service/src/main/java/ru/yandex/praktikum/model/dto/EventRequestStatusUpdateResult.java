package ru.yandex.praktikum.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private ParticipantRequestDto confirmedRequests;
    private ParticipantRequestDto rejectedRequests;
}
