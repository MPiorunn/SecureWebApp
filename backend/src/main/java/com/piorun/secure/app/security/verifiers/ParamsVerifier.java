package com.piorun.secure.app.security.verifiers;

import org.springframework.stereotype.Component;

@Component
public class ParamsVerifier {

    private final PasswordVerifier passwordVerifier;
    private final UsernameVerifier usernameVerifier;
    private final EmailVerifier emailVerifier;

    public ParamsVerifier() {
        this.passwordVerifier = new PasswordVerifier();
        this.usernameVerifier = new UsernameVerifier();
        this.emailVerifier = new EmailVerifier();
    }

    public void verifyInputParameters(String username, String password, String email) {
        usernameVerifier.verify(username);
        passwordVerifier.verify(password);
        emailVerifier.verify(email);
    }

    public void verifyInputParameters(String username, String password) {
        usernameVerifier.verify(username);
        passwordVerifier.verify(password);
    }

}
