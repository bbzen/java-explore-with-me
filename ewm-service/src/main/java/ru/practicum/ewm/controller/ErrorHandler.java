package ru.practicum.ewm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.OnConflictException;
import ru.practicum.ewm.model.ApiError;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleParamException(final MissingServletRequestParameterException e) {
        ApiError apiError = new ApiError();

        apiError.setReason("Some of mandatory params are missed");
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setMessage(e.getLocalizedMessage());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        ApiError apiError = new ApiError();

        apiError.setReason("Some of method arguments are not valid.");
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setMessage(e.getLocalizedMessage());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleValidationException(final OnConflictException e) {
        ApiError apiError = new ApiError();

        apiError.setReason("Incorrectly made request.");
        apiError.setStatus(HttpStatus.CONFLICT);
        apiError.setMessage(e.getLocalizedMessage());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        ApiError apiError = new ApiError();

        apiError.setReason("Entity somehow was not found.");
        apiError.setStatus(HttpStatus.CONFLICT);
        apiError.setMessage(e.getLocalizedMessage());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidParamsException(final BadRequestException e) {
        ApiError apiError = new ApiError();

        apiError.setReason("Entity somehow was not found.");
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setMessage(e.getLocalizedMessage());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternalServerErrorException(final Exception e) {
        ApiError apiError = new ApiError();

        apiError.setReason("Internal ewm server error.");
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setMessage(e.getLocalizedMessage());
        return apiError;
    }
}
