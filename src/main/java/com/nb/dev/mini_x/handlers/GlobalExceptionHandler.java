package com.nb.dev.mini_x.handlers;

import com.nb.dev.mini_x.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidLoginExeption.class)
    public ResponseEntity<DefaultErrorBody> invalidLoginExceptionHandler(InvalidLoginExeption ex){

        DefaultErrorBody exBody = new DefaultErrorBody();
        exBody.setTimesTamp(LocalDateTime.now());
        exBody.setStatus(HttpStatus.UNAUTHORIZED.value());
        exBody.setError("unauthorized login");
        exBody.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exBody);

    }

    @ExceptionHandler(UserNameUnavailableException.class)
    public ResponseEntity<DefaultErrorBody> userNameUnavailableExceptionHandler(UserNameUnavailableException ex){

        DefaultErrorBody exBody = new DefaultErrorBody();
        exBody.setTimesTamp(LocalDateTime.now());
        exBody.setStatus(HttpStatus.CONFLICT.value());
        exBody.setError("username unavailable");
        exBody.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(exBody);

    }

    @ExceptionHandler(UnauthorizedDeletionException.class)
    public ResponseEntity<DefaultErrorBody> unauthorizedDeletionExceptionHandler(UnauthorizedDeletionException ex){

        DefaultErrorBody exBody = new DefaultErrorBody();
        exBody.setTimesTamp(LocalDateTime.now());
        exBody.setStatus(HttpStatus.UNAUTHORIZED.value());
        exBody.setError("unauthorized deletion");
        exBody.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exBody);

    }

    @ExceptionHandler(TweetNotFoundException.class)
    public ResponseEntity<DefaultErrorBody> tweetNotFoundExceptionHandler(TweetNotFoundException ex){

        DefaultErrorBody exBody = new DefaultErrorBody();
        exBody.setTimesTamp(LocalDateTime.now());
        exBody.setStatus(HttpStatus.NOT_FOUND.value());
        exBody.setError("tweet not found");
        exBody.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exBody);

    }

}

