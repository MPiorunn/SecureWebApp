package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.VerificationException;

public interface Verifier {

    void verify(String input) throws VerificationException;
}
