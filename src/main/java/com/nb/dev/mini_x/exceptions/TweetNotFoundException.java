package com.nb.dev.mini_x.exceptions;

public class TweetNotFoundException extends RuntimeException {
    public TweetNotFoundException(String message) {
        super(message);
    }
}
