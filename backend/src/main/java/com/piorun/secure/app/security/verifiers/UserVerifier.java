package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.SignInException;
import com.piorun.secure.app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;

@Component
public class UserVerifier implements Verifier {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserVerifier.class);

    public UserVerifier(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void verify(String username) throws SignInException {

        try {
            userRepository.findByUsername(username);
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.info("User with " + username + " already exists in database");
            throw new SignInException("Username already taken");
        }
    }
}
