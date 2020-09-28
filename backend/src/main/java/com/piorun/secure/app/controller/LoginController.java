package com.piorun.secure.app.controller;

import com.piorun.secure.app.exception.LoginException;
import com.piorun.secure.app.exception.VerificationException;
import com.piorun.secure.app.model.Salt;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.repository.SaltRepository;
import com.piorun.secure.app.repository.UserRepository;
import com.piorun.secure.app.security.PasswordUtils;
import com.piorun.secure.app.security.verifiers.ParamsVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final ParamsVerifier verifier;
    private final SaltRepository saltRepository;
    private final UserRepository userRepository;

    public LoginController(ParamsVerifier verifier, SaltRepository saltRepository, UserRepository userRepository) {
        this.verifier = verifier;
        this.saltRepository = saltRepository;
        this.userRepository = userRepository;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<User> login(String username, String password) {
        logger.info("Received login request with username : " + username + " and password : " + password);

        verifyInputParameters(username, password);

        User user = getUserFromDatabase(username);

        Salt salt = getSaltFromDatabase(user.getSaltId());

        verifyProvidedPassword(user.getHash(), password, salt.getValue());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private void verifyProvidedPassword(String hash, String password, String salt) {
        logger.info("Hash verification started for hash " + hash);
        boolean checkHash = PasswordUtils.checkHash(hash, password, salt);
        if (!checkHash) {
            logger.info("Hash incorrect. Aborting login attempt");
            throw new LoginException();
        }
        logger.info("Hash correct, login successful");
    }

    private Salt getSaltFromDatabase(String saltId) {
        Optional<Salt> saltOptional = saltRepository.findById(saltId);
        if (saltOptional.isEmpty()) {
            throw new LoginException();
        }
        return saltOptional.get();
    }

    private User getUserFromDatabase(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            logger.info("User with username " + username + " not found in database");
            logger.info("Calculating some hash to prevent timing attack");
            PasswordUtils.checkHash("#@! Response time attack PR0tection", "#@! Response time attack PR0tection", PasswordUtils.generateSalt());
            throw new LoginException();
        }
        logger.info("User with username " + username + " found in database");
        return userOptional.get();
    }

    private void verifyInputParameters(String username, String password) {
        try {
            verifier.verifyInputParameters(username, password);
        } catch (VerificationException e) {
            throw new LoginException();
        }
    }
}
