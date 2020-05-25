package com.piorun.secure.app.controller;

import com.piorun.secure.app.exception.LoginException;
import com.piorun.secure.app.exception.VerificationException;
import com.piorun.secure.app.model.Salt;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.repository.SaltRepository;
import com.piorun.secure.app.repository.UserRepository;
import com.piorun.secure.app.security.PasswordUtils;
import com.piorun.secure.app.security.verifiers.ParamsVerifier;
import com.piorun.secure.app.security.verifiers.UserVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final ParamsVerifier verifier;
    private final UserVerifier userVerifier;
    private final SaltRepository saltRepository;
    private final UserRepository userRepository;

    public LoginController(ParamsVerifier verifier, UserVerifier userVerifier, SaltRepository saltRepository, UserRepository userRepository) {
        this.verifier = verifier;
        this.userVerifier = userVerifier;
        this.saltRepository = saltRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(String username, String password) {
        logger.info("Received login request with username : " + username + " and password : " + password);

        verifyInputParameters(username, password);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new LoginException();
        }
        User user = userOptional.get();
        logger.info("User with username + " + username + " found in db");
        Optional<Salt> saltOptional = saltRepository.findById(user.getSaltId());
        if (saltOptional.isEmpty()) {
            throw new LoginException();
        }
        logger.info("Salt found for username " + username);
        Salt salt = saltOptional.get();

        logger.info("Hash verification for user " + username + " ...");
        boolean checkHash = PasswordUtils.checkHash(user.getHash(), password, salt.getValue());
        if (!checkHash) {
            logger.info("Hash incorrect");
            throw new LoginException();
        }
        logger.info("Hash correct, login successful");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    private void verifyInputParameters(String username, String password) {
        try {
            verifier.verifyInputParameters(username, password);
        } catch (VerificationException e) {
            throw new LoginException();
        }
    }
}
