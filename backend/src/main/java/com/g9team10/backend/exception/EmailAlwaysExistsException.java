package com.g9team10.backend.exception;

public class EmailAlwaysExistsException extends BusinessException {
    public EmailAlwaysExistsException() {
        super("Email always exists");
    }
}
