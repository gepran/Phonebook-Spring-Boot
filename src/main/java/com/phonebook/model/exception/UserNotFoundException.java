package com.phonebook.model.exception;


import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException{
    private final String errorMessage;

    public UserNotFoundException(String errorMessage) {
        super("Person could not be found. " + errorMessage);
        this.errorMessage = errorMessage;
    }
}
