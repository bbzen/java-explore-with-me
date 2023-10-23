package ru.practicum.ewm.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateCommentDto {
    @NotBlank
    @Size(min = 1, max = 1000, message = "Invalid comment length.")
    private String text;
}
