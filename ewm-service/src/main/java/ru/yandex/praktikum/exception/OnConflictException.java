package ru.yandex.praktikum.exception;

public class OnConflictException extends RuntimeException {
    public OnConflictException(String message) {
        super(message);
    }
}
