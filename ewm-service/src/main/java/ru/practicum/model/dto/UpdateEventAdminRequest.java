package ru.practicum.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.model.status.StateAction;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest implements DatedEvent {
    @Size(min = 20, max = 2000, message = "Annotation must be more than 20 and less tan 2000 chars.")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Description must be more than 20 and less tan 7000 chars.")
    private String description;
    @Future(message = "Event date must be in future.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private NewLocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    @Size(min = 3, max = 120, message = "Title must be more than 3 and less tan 120 chars.")
    private String title;
}
