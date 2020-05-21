package com.piorun.secure.app.security;

import com.piorun.secure.app.exception.IncorrectPasswordException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordVerifier {

    public static final int MIN_LENGTH = 10;

    public static void checkPassword(String password) throws IncorrectPasswordException {

        checkLength(password);
        checkIfContainsUpperOrLowercase(password);
        checkIfContainsDigit(password);
        checkIfContainsSpecialCharacter(password);
    }

    private static void checkIfContainsSpecialCharacter(String password) throws IncorrectPasswordException {
        Pattern digit = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher hasDigit = digit.matcher(password);
        if (!hasDigit.find()) {
            throw new IncorrectPasswordException("Password must contain at least one special character");
        }
    }

    private static void checkIfContainsDigit(String password) throws IncorrectPasswordException {
        Pattern digit = Pattern.compile("[0-9]");
        Matcher hasDigit = digit.matcher(password);
        if (!hasDigit.find()) {
            throw new IncorrectPasswordException("Password must contain at least one digit");
        }

    }

    private static void checkLength(String password) throws IncorrectPasswordException {
        if (password.length() < MIN_LENGTH) {
            throw new IncorrectPasswordException("Password length must be at least 10 characters");
        }
    }

    private static void checkIfContainsUpperOrLowercase(String password) throws IncorrectPasswordException {
        if (password.toLowerCase().equals(password)) {
            throw new IncorrectPasswordException("Password must contain at least one uppercase character");
        }
        if (password.toUpperCase().equals(password)) {
            throw new IncorrectPasswordException("Password must contain at least one uppercase character");
        }
    }

}
