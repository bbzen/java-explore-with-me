package ru.practicum.ewm.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto implements DatedEvent {
    @NotBlank(message = "Invalid event annotation.")
    @Size(min = 20, message = "Event annotation must be more than 20 char.")
    @Size(max = 2000, message = "Event annotation must be less than 2000 char.")
    private String annotation;
    @NotNull(message = "Category cant be null")
    private Long category;
    @NotBlank(message = "Invalid event description.")
    @Size(min = 20, message = "Event description must be more than 20 char.")
    @Size(max = 7000, message = "Event description must be less than 7000 char.")
    private String description;
    @Future(message = "Event must be in future and in two hours before present moment.")
    @NotNull(message = "Event date cant be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private NewLocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotBlank(message = "Invalid event description.")
    @Size(min = 3, message = "Event description must be more than 20 char.")
    @Size(max = 120, message = "Event description must be less than 7000 char.")
    private String title;
}
