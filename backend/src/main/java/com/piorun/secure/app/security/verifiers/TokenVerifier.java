package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.VerificationException;
import org.springframework.stereotype.Component;

@Component
public class TokenVerifier implements Verifier {




    @Override
    public void verify(String token) throws VerificationException {

    }
}
