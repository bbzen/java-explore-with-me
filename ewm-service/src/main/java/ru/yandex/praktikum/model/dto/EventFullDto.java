package ru.yandex.praktikum.model.dto;

import lombok.Data;
import ru.yandex.praktikum.model.Location;
import ru.yandex.praktikum.model.status.EventStatus;

import java.time.LocalDateTime;

@Data
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantsLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private EventStatus state;
    private String title;
    private Long views;
}
