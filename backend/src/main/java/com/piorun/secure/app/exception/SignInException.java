package com.piorun.secure.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SignInException extends IllegalArgumentException{

    public SignInException(String message) {
        super(message);
    }
}
