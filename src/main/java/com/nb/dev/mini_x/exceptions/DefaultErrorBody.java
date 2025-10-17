package com.nb.dev.mini_x.exceptions;

import java.time.LocalDateTime;

public class DefaultErrorBody {
    private LocalDateTime timesTamp;
    private int status;
    private String error;
    private String message;

    public DefaultErrorBody(){}

    public DefaultErrorBody(LocalDateTime timesTamp, int status, String error, String message) {
        this.timesTamp = timesTamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public LocalDateTime getTimesTamp() {
        return timesTamp;
    }

    public void setTimesTamp(LocalDateTime timesTamp) {
        this.timesTamp = timesTamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
