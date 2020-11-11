package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.VerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UsernameFormatVerifier implements Verifier {

    private static final int MIN_LENGTH = 6;
    private static final Logger logger = LoggerFactory.getLogger(UsernameFormatVerifier.class);

    @Override
    public void verify(String username) throws VerificationException {
        if (username.length() < MIN_LENGTH) {
            logger.info("Username " + username + " must be at least " + MIN_LENGTH + " characters long");
            throw new VerificationException("Username must be at least " + MIN_LENGTH + " characters long");
        }

        logger.info("Username " + username + " successfully verified");
    }

}
