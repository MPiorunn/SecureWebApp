package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.SignInException;


public class UsernameVerifier implements Verifier {

    private static final int MIN_LENGTH = 6;

    @Override
    public void verify(String username) throws SignInException {
        if (username.length() < MIN_LENGTH) {
            throw new SignInException("Username must be at least " + MIN_LENGTH + " characters long");
        }
    }

}
