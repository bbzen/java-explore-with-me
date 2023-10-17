package ru.practicum.exception;

public class OnConflictException extends RuntimeException {
    public OnConflictException(String message) {
        super(message);
    }
}
