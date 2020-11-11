package com.piorun.secure.app.security.verifiers;

import org.springframework.stereotype.Component;

@Component
public class ParamsVerifier {

    private final PasswordVerifier passwordVerifier;
    private final UsernameFormatVerifier usernameVerifier;
    private final EmailFormatVerifier emailFormatVerifier;

    public ParamsVerifier() {
        this.passwordVerifier = new PasswordVerifier();
        this.usernameVerifier = new UsernameFormatVerifier();
        this.emailFormatVerifier = new EmailFormatVerifier();
    }

    public void verifyInputParameters(String username, String password, String email) {
        usernameVerifier.verify(username);
        passwordVerifier.verify(password);
        emailFormatVerifier.verify(email);
    }

    public void verifyInputParameters(String username, String password) {
        usernameVerifier.verify(username);
        passwordVerifier.verify(password);
    }

}
