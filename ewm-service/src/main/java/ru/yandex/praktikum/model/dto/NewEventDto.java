package ru.yandex.praktikum.model.dto;

import lombok.Data;
import ru.yandex.praktikum.model.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class NewEventDto implements DatedEvent {
    @NotNull(message = "Invalid event annotation.")
    @Size(min = 20, message = "Event annotation must be more than 20 char.")
    @Size(max = 2000, message = "Event annotation must be less than 2000 char.")
    private String annotation;
    private Long category;
    @NotNull(message = "Invalid event description.")
    @Size(min = 20, message = "Event description must be more than 20 char.")
    @Size(max = 7000, message = "Event description must be less than 7000 char.")
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantsLimit;
    private Boolean requestModeration;
    @NotNull(message = "Invalid event description.")
    @Size(min = 3, message = "Event description must be more than 20 char.")
    @Size(max = 120, message = "Event description must be less than 7000 char.")
    private String title;
}
