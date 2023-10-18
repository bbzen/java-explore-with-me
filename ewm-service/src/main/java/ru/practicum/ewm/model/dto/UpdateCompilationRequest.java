package ru.practicum.ewm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    private List<Long> events;
    private Boolean pinned;
    @NotNull(message = "Invalid compilation title.")
    @Size(min = 1, message = "Compilation`s title cant be less than 1 char.")
    @Size(max = 50, message = "Compilation`s title cant be more than 50 chars.")
    private String title;
}
