package ru.yandex.praktikum.model.dto;

import lombok.Data;
import ru.yandex.praktikum.model.Location;
import ru.yandex.praktikum.model.status.StateAction;

import java.time.LocalDateTime;

@Data
public class UpdateEventUserRequest implements DatedEvent {
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantsLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;
}
