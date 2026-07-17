package com.g9team10.backend.exception;

public class InvalidUserContentTagException extends BusinessException {

    public InvalidUserContentTagException() {
        super("Personal tag must contain at least one valid letter or number");
    }
}