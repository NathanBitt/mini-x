package com.nb.dev.mini_x.exceptions;

public class UserNameUnavailableException extends RuntimeException{

    public UserNameUnavailableException(String message) {
        super(message);
    }
}
