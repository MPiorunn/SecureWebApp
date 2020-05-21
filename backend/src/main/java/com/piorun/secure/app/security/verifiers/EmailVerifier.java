package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.SignInException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailVerifier implements Verifier {

    @Override
    public void verify(String email) throws SignInException {
        try {
            InternetAddress address = new InternetAddress(email);
            address.validate();
        } catch (AddressException e) {
            throw new SignInException("Wrong email format");
        }

        if (!email.contains(".") || !email.contains("@")) {
            throw new SignInException("Wrong email format");
        }
    }
}
