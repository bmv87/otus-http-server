package ru.otus.java.basic.http.server.exceptions;

public class NotFoundException  extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
