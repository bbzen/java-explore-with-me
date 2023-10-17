package ru.practicum.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewLocationDto {
    @NotNull(message = "Location`s lat cat be null")
    private Float lat;
    @NotNull(message = "Location`s lon cat be null")
    private Float lon;
}
