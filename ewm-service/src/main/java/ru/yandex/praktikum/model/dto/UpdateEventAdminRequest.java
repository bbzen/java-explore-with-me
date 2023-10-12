package ru.yandex.praktikum.model.dto;

import lombok.Data;
import ru.yandex.praktikum.model.Location;
import ru.yandex.praktikum.model.StateAction;

@Data
public class UpdateEventAdminRequest {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantsLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;
}
