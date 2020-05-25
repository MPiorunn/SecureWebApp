package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.VerificationException;
import org.junit.Test;

public class UsernameVerifierTest {

    private final Verifier usernameVerifier = new UsernameVerifier();

    @Test(expected = VerificationException.class)
    public void failOnTooShortUsername(){
        usernameVerifier.verify("dsa");
    }

    @Test
    public void passOnCorrectUsername(){
        usernameVerifier.verify("dLSAdma2lidM2");
    }
}