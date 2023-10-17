package ru.practicum.ewm.exception;

public class OnConflictException extends RuntimeException {
    public OnConflictException(String message) {
        super(message);
    }
}
