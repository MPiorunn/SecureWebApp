package com.piorun.secure.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordResetException extends IllegalArgumentException {

    public PasswordResetException(String s) {
        super(s);
    }
}