package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.SignInException;

public interface Verifier {

    void verify(String input) throws SignInException;
}
