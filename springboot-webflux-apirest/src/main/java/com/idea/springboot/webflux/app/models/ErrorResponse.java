package com.idea.springboot.webflux.app.models;

import java.util.List;

public class ErrorResponse implements GenericMessageResponse {
    private String message;
    private String timestamp;
    private List<String> errors;

    public ErrorResponse(String message, String timestamp, List<String> errors) {
        this.message = message;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
