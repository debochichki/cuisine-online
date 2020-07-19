package com.softuni.cuisineonline.errors;

public class MissingVideoException extends RuntimeException {

    public MissingVideoException(String message) {
        super(message);
    }

    public MissingVideoException(String message, Throwable cause) {
        super(message, cause);
    }
}
