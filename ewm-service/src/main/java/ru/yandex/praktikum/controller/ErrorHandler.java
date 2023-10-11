package ru.yandex.praktikum.controller;

import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yandex.praktikum.model.ApiError;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    public @NonNull ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                        @NonNull HttpHeaders headers,
                                                                        @NonNull HttpStatus status,
                                                                        @NonNull WebRequest request) {
        ApiError apiError = new ApiError();
        List<String> errors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + " : " + error.getRejectedValue());
        }

        apiError.setErrors(errors);
        apiError.setReason("Incorrectly made request.");
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setMessage(e.getLocalizedMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


}
