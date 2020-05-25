package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.VerificationException;
import org.junit.Test;

public class EmailVerifierTest {

    private final Verifier verifier = new EmailVerifier();

    @Test(expected = VerificationException.class)
    public void failOnNoAtSign(){
        verifier.verify("emailNoAt");
    }

    @Test(expected = VerificationException.class)
    public void failOnNothingAfterAt(){
        verifier.verify("emailNoAt@");
    }

    @Test(expected = VerificationException.class)
    public void shouldFailOnNoDot(){
        verifier.verify("emailNoAt@dsadas");
    }
}