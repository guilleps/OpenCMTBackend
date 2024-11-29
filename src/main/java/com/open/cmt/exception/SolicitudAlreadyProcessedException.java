package com.open.cmt.exception;

public class SolicitudAlreadyProcessedException extends RuntimeException {
    public SolicitudAlreadyProcessedException(String message) {
        super(message);
    }
}
