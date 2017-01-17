package com.soldiersofmobile.todoexpert.api;

public class ErrorResponse {

    private int code;
    private String error;

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }
}
