package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.SignInException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PasswordVerifier implements Verifier {

    public static final int MIN_LENGTH = 10;



    @Override
    public void verify(String password) throws SignInException {

        checkLength(password);
        checkIfContainsUpperOrLowercase(password);
        checkIfContainsDigit(password);
        checkIfContainsSpecialCharacter(password);
    }

    private static void checkIfContainsSpecialCharacter(String password) throws SignInException {
        Pattern digit = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher hasDigit = digit.matcher(password);
        if (!hasDigit.find()) {
            throw new SignInException("Password must contain at least one special character");
        }
    }

    private static void checkIfContainsDigit(String password) throws SignInException {
        Pattern digit = Pattern.compile("[0-9]");
        Matcher hasDigit = digit.matcher(password);
        if (!hasDigit.find()) {
            throw new SignInException("Password must contain at least one digit");
        }

    }

    private static void checkLength(String password) throws SignInException {
        if (password.length() < MIN_LENGTH) {
            throw new SignInException("Password length must be at least " + MIN_LENGTH + " characters");
        }
    }

    private static void checkIfContainsUpperOrLowercase(String password) throws SignInException {
        if (password.toLowerCase().equals(password)) {
            throw new SignInException("Password must contain at least one uppercase character");
        }
        if (password.toUpperCase().equals(password)) {
            throw new SignInException("Password must contain at least one uppercase character");
        }
    }

}