package ru.yandex.praktikum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class ApiError {
    private List<String> errors;
    private String message;
    private String reason;
    private HttpStatus status;
    private final String timestamp;

    public ApiError() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
