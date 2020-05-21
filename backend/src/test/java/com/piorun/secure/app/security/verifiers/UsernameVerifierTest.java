package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.SignInException;
import org.junit.Test;

public class UsernameVerifierTest {

    private final Verifier usernameVerifier = new UsernameVerifier();

    @Test(expected = SignInException.class)
    public void failOnTooShortUsername(){
        usernameVerifier.verify("dsa");
    }

    @Test
    public void passOnCorrectUsername(){
        usernameVerifier.verify("dLSAdma2lidM2");
    }
}