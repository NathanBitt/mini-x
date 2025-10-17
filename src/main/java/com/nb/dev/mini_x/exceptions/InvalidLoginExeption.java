package com.nb.dev.mini_x.exceptions;

public class InvalidLoginExeption extends RuntimeException {
    public InvalidLoginExeption(String message) {
        super(message);
    }
}
