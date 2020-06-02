package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.VerificationException;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserVerifier implements Verifier {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserVerifier.class);

    public UserVerifier(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void verifyIfUserExists(String username, String email) {
        verifyByUsername(username);
        verifyByEmail(email);
        logger.info("User with " + username + " or " + email + " not found in the database. Verification successful");
    }

    public void verifyByUsername(String username) {
        try {
            verify(username);
        } catch (VerificationException e) {
            throw new VerificationException("Username already taken");
        }
    }

    private void verifyByEmail(String email) throws VerificationException {
        try {
            verify(email);
        } catch (VerificationException e) {
            throw new VerificationException("Email already taken");
        }
    }

    @Override
    public void verify(String input) throws VerificationException {

        try {
            Optional<User> user = userRepository.findByUsername(input);
            if (user.isPresent()) {
                logger.info("User with " + input + " already exists in database");
                throw new VerificationException("");
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.info("User with " + input + " already exists in database");
            throw new VerificationException("");
        }
    }

}
