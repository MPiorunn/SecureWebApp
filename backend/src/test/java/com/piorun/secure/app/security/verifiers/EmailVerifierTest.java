package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.SignInException;
import org.junit.Test;

public class EmailVerifierTest {

    private final Verifier verifier = new EmailVerifier();

    @Test(expected = SignInException.class)
    public void failOnNoAtSign(){
        verifier.verify("emailNoAt");
    }

    @Test(expected = SignInException.class)
    public void failOnNothingAfterAt(){
        verifier.verify("emailNoAt@");
    }

    @Test(expected = SignInException.class)
    public void shouldFailOnNoDot(){
        verifier.verify("emailNoAt@dsadas");
    }
}