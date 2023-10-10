package ru.yandex.praktikum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.praktikum.model.ApiError;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onValidationException(final ValidationException e) {
        ApiError apiError = new ApiError();
        apiError.setReason("Incorrectly made request.");
        apiError.setStatus(HttpStatus.BAD_REQUEST.toString());
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return apiError;
    }
}
