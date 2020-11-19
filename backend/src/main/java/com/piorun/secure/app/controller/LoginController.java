package com.piorun.secure.app.controller;

import com.piorun.secure.app.exception.LoginException;
import com.piorun.secure.app.exception.VerificationException;
import com.piorun.secure.app.model.Salt;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.repository.SaltRepository;
import com.piorun.secure.app.repository.UserRepository;
import com.piorun.secure.app.security.PasswordUtils;
import com.piorun.secure.app.security.verifiers.ParamsVerifier;
import com.piorun.secure.app.service.SaltService;
import com.piorun.secure.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final ParamsVerifier verifier;
    private final SaltService saltService;
    private final UserService userService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<User> login(String username, String password) {
        log.info("Received login request with username : " + username + " and password : " + password);

        verifyInputParameters(username, password);

        User user = getUserFromDatabase(username);

        Salt salt = getSaltFromDatabase(user.getSaltId());

        verifyProvidedPassword(user.getHash(), password, salt.getValue());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private void verifyProvidedPassword(String hash, String password, String salt) {
        log.info("Hash verification started for hash " + hash);
        boolean checkHash = PasswordUtils.checkHash(hash, password, salt);
        if (!checkHash) {
            log.info("Hash incorrect. Aborting login attempt");
            throw new LoginException();
        }
        log.info("Hash correct, login successful");
    }

    private Salt getSaltFromDatabase(String saltId) {
        Optional<Salt> saltOptional = saltService.findById(saltId);
        if (saltOptional.isEmpty()) {
            throw new LoginException();
        }
        return saltOptional.get();
    }

    private User getUserFromDatabase(String username) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            log.info("User with username " + username + " not found in database");
            log.info("Calculating some hash to prevent timing attack");
            PasswordUtils.checkHash("#@! Response time attack PR0tection", "#@! Response time attack PR0tection", PasswordUtils.generateSalt());
            throw new LoginException();
        }
        log.info("User with username " + username + " found in database");
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
