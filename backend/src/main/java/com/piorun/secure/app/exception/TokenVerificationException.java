package com.piorun.secure.app.exception;

public class TokenVerificationException extends IllegalArgumentException {
    public TokenVerificationException(String msg) {
        super(msg);
    }
}
