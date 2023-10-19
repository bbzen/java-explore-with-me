package ru.practicum.ewm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank(message = "Invalid user`s email.")
    @Size(min = 6, message = "Email length cant be less than 6 chars.")
    @Size(max = 254, message = "Email length cant be more than 254 chars.")
    @Email(message = "Given line is not an email.")
    private String email;
    @NotBlank(message = "Invalid user`s name.")
    @Size(min = 2, message = "User`s name length cant be less than 2 chars.")
    @Size(max = 250, message = "User`s name length cant be more than 250 chars.")
    private String name;
}
