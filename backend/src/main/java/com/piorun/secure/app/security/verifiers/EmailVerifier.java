package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.SignInException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailVerifier implements Verifier {


    private static final Logger logger = LoggerFactory.getLogger(EmailVerifier.class);

    @Override
    public void verify(String email) throws SignInException {
        try {
            InternetAddress address = new InternetAddress(email);
            address.validate();
        } catch (AddressException e) {
            logger.info("Wrong email format for email : " + email);
            throw new SignInException("Wrong email format");
        }

        if (!email.contains(".") || !email.contains("@")) {
            logger.info("Wrong email format for email : " + email);
            throw new SignInException("Wrong email format");
        }

        logger.info("Email " + email + " successfully verified");
    }
}
