package com.piorun.secure.app.controller;

import com.piorun.secure.app.exception.SignInException;
import com.piorun.secure.app.exception.VerificationException;
import com.piorun.secure.app.model.Salt;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.security.PasswordUtils;
import com.piorun.secure.app.security.verifiers.ParamsVerifier;
import com.piorun.secure.app.security.verifiers.UserVerifier;
import com.piorun.secure.app.service.SaltService;
import com.piorun.secure.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class SignInController {

    private final ParamsVerifier verifier;
    private final UserVerifier userVerifier;
    private final SaltService saltService;
    private final UserService userService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/add")
    public ResponseEntity<User> registerUser(String username, String password, String email) {
        if (log.isInfoEnabled()) {
            log.info("Received sign in request for username : " + username + " and email : " + email);
        }
        verifyInputParameters(username, password, email);
        String s = PasswordUtils.generateSalt();
        Salt salt = new Salt(s);
        String hash = PasswordUtils.hashPassword(password, s);
        Salt savedSalt = saltService.save(salt);
        User user = new User(username, hash, email, savedSalt.getId());
        userService.save(user);

        log.info("Added new user " + username + " to the database");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private void verifyInputParameters(String username, String password, String email) {
        try {
            verifier.verifyInputParameters(username, password, email);
            userVerifier.verifyIfUserExists(username, email);
        } catch (VerificationException e) {
            throw new SignInException(e.getMessage());
        }
    }
}
