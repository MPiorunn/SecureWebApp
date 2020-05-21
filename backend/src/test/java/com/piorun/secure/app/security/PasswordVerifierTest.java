package com.piorun.secure.app.security;

import com.piorun.secure.app.exception.SignInException;
import com.piorun.secure.app.security.verifiers.PasswordVerifier;
import org.junit.Test;

public class PasswordVerifierTest {

    @Test(expected = SignInException.class)
    public void shouldFailOnTooShortPassword() throws SignInException {
        String password = "1233456";
        PasswordVerifier.checkPassword(password);
    }

    @Test(expected = SignInException.class)
    public void shouldFailOnNoUppercase() throws SignInException {
        String password = "asda2222####2";
        PasswordVerifier.checkPassword(password);
    }

    @Test(expected = SignInException.class)
    public void shouldFailOnNoDigit() throws SignInException {
        String password = "mdskalsdjalskd!!!!!";
        PasswordVerifier.checkPassword(password);
    }

    @Test(expected = SignInException.class)
    public void shouldFailOnNoSpecial() throws SignInException {
        String password = "adn28dja2o8ajJdD";
        PasswordVerifier.checkPassword(password);
    }

    @Test(expected = SignInException.class)
    public void shouldFailOnNoLowercase() throws SignInException {
        String password = "DDDDDD22222@@@@";
        PasswordVerifier.checkPassword(password);
    }

    @Test
    public void shouldPassOnCorrectPassword() throws SignInException {
        String password = "Passw0rddd!";
        PasswordVerifier.checkPassword(password);
    }


}