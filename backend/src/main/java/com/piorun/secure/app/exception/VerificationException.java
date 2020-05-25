package com.piorun.secure.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class VerificationException extends IllegalArgumentException {

    public VerificationException(String message) {
        super(message);
    }
}
