package com.keycloakaccountplus.exception;

public class KeycloakApiException extends RuntimeException {

    private final int statusCode;

    public KeycloakApiException(String message) {
        super(message);
        this.statusCode = 500;
    }

    public KeycloakApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public KeycloakApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
