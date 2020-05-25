package com.piorun.secure.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LoginException extends IllegalArgumentException {

    public LoginException() {
        super("Wrong username or password");
    }
}
