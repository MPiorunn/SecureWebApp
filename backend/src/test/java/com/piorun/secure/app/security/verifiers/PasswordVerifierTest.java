package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.VerificationException;
import org.junit.Test;

public class PasswordVerifierTest {

    private final Verifier verifier = new PasswordVerifier();

    @Test(expected = VerificationException.class)
    public void shouldFailOnTooShortPassword() throws VerificationException {
        String password = "1233456";
        verifier.verify(password);
    }

    @Test(expected = VerificationException.class)
    public void shouldFailOnNoUppercase() throws VerificationException {
        String password = "asda2222####2";
        verifier.verify(password);
    }

    @Test(expected = VerificationException.class)
    public void shouldFailOnNoDigit() throws VerificationException {
        String password = "mdskalsdjalskd!!!!!";
        verifier.verify(password);
    }

    @Test(expected = VerificationException.class)
    public void shouldFailOnNoSpecial() throws VerificationException {
        String password = "adn28dja2o8ajJdD";
        verifier.verify(password);
    }

    @Test(expected = VerificationException.class)
    public void shouldFailOnNoLowercase() throws VerificationException {
        String password = "DDDDDD22222@@@@";
        verifier.verify(password);
    }

    @Test
    public void shouldPassOnCorrectPassword() throws VerificationException {
        String password = "Passw0rddd!";
        verifier.verify(password);
    }


}