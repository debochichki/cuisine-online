package com.softuni.cuisineonline.errors;

public class MissingEntityException extends RuntimeException {

    public MissingEntityException(String message) {
        super(message);
    }

    public MissingEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
