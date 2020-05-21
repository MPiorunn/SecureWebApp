package com.piorun.secure.app.security;

import com.piorun.secure.app.exception.IncorrectPasswordException;
import org.junit.Test;

public class PasswordVerifierTest {

    @Test(expected = IncorrectPasswordException.class)
    public void shouldFailOnTooShortPassword() throws IncorrectPasswordException {
        String password = "1233456";
        PasswordVerifier.checkPassword(password);
    }

    @Test(expected = IncorrectPasswordException.class)
    public void shouldFailOnNoUppercase() throws IncorrectPasswordException {
        String password = "asda2222####2";
        PasswordVerifier.checkPassword(password);
    }

    @Test(expected = IncorrectPasswordException.class)
    public void shouldFailOnNoDigit() throws IncorrectPasswordException {
        String password = "mdskalsdjalskd!!!!!";
        PasswordVerifier.checkPassword(password);
    }

    @Test(expected = IncorrectPasswordException.class)
    public void shouldFailOnNoSpecial() throws IncorrectPasswordException {
        String password = "adn28dja2o8ajJdD";
        PasswordVerifier.checkPassword(password);
    }

    @Test(expected = IncorrectPasswordException.class)
    public void shouldFailOnNoLowercase() throws IncorrectPasswordException {
        String password = "DDDDDD22222@@@@";
        PasswordVerifier.checkPassword(password);
    }

    @Test
    public void shouldPassOnCorrectPassword() throws IncorrectPasswordException {
        String password = "Passw0rddd!";
        PasswordVerifier.checkPassword(password);
    }


}