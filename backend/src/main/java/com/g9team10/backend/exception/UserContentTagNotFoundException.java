package com.g9team10.backend.exception;

public class UserContentTagNotFoundException extends ResourceNotFoundException {

    public UserContentTagNotFoundException(Long tagId) {
        super("Personal tag with id " + tagId + " not found");
    }
}