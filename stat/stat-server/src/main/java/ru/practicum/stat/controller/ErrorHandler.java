package ru.practicum.stat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.stat.exception.HitInputDataInvalidException;
import ru.practicum.stat.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleStatInputDtoInvalidException(final HitInputDataInvalidException e) {
        return new ErrorResponse(e.getMessage());
    }
}
