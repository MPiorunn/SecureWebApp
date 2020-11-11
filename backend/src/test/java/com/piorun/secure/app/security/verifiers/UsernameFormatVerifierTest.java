package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.VerificationException;
import org.junit.Test;

public class UsernameFormatVerifierTest {

    private final Verifier usernameVerifier = new UsernameFormatVerifier();

    @Test(expected = VerificationException.class)
    public void failOnTooShortUsername(){
        usernameVerifier.verify("dsa");
    }

    @Test
    public void passOnCorrectUsername(){
        usernameVerifier.verify("dLSAdma2lidM2");
    }
}