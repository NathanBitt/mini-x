package com.nb.dev.mini_x.exceptions;

public class UnauthorizedDeletionExeption extends RuntimeException {
    public UnauthorizedDeletionExeption(String message) {
        super(message);
    }
}
