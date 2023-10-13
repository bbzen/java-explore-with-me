package ru.yandex.praktikum.exception;

public class EntityValidationException extends RuntimeException {
    public EntityValidationException(String message) {
        super(message);
    }
}
