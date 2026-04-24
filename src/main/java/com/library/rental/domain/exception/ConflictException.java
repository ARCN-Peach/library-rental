package com.library.rental.domain.exception;

public class ConflictException extends BusinessException {

    public ConflictException(String message) {
        super(message);
    }
}