package com.nb.dev.mini_x.exceptions;

public class UnauthorizedDeletionException extends RuntimeException {
    public UnauthorizedDeletionException(String message) {
        super(message);
    }
}
