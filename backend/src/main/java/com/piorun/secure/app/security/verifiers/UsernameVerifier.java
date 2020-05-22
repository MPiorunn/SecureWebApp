package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.SignInException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UsernameVerifier implements Verifier {

    private static final int MIN_LENGTH = 6;
    private static final Logger logger = LoggerFactory.getLogger(UsernameVerifier.class);

    @Override
    public void verify(String username) throws SignInException {
        if (username.length() < MIN_LENGTH) {
            logger.info("Username " + username + " must be at least " + MIN_LENGTH + " characters long");
            throw new SignInException("Username must be at least " + MIN_LENGTH + " characters long");
        }
    }

}
