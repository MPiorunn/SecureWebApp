package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.VerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordVerifier implements Verifier {

    public static final int MIN_LENGTH = 10;
    private static final Logger logger = LoggerFactory.getLogger(PasswordVerifier.class);
    private static final Pattern SPECIAL_CHAR = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
    private static final Pattern DIGIT = Pattern.compile("[0-9]");

    @Override
    public void verify(String password) throws VerificationException {

        checkLength(password);
        checkIfContainsUpperOrLowercase(password);
        checkIfContainsDigit(password);
        checkIfContainsSpecialCharacter(password);

        logger.info("Password successfully verified");
    }

    private static void checkLength(String password) throws VerificationException {
        if (password.length() < MIN_LENGTH) {
            logger.info("Password must be at least " + MIN_LENGTH + " characters long");
            throw new VerificationException("Password length must be at least " + MIN_LENGTH + " characters");
        }
    }

    private static void checkIfContainsUpperOrLowercase(String password) throws VerificationException {
        if (password.toLowerCase().equals(password)) {
            logger.info("Password must contain at least one uppercase character");
            throw new VerificationException("Password must contain at least one uppercase character");
        }
        if (password.toUpperCase().equals(password)) {
            logger.info("Password  must contain at least one lowercase character");
            throw new VerificationException("Password must contain at least one lowercase character");
        }
    }

    private static void checkIfContainsDigit(String password) throws VerificationException {
        Matcher hasDigit = DIGIT.matcher(password);
        if (!hasDigit.find()) {
            logger.info("Password must contain at least one digit");
            throw new VerificationException("Password must contain at least one digit");
        }
    }

    private static void checkIfContainsSpecialCharacter(String password) throws VerificationException {
        Matcher hasDigit = SPECIAL_CHAR.matcher(password);
        if (!hasDigit.find()) {
            logger.info("Password  must contain at least one special character");
            throw new VerificationException("Password must contain at least one special character");
        }
    }
}
