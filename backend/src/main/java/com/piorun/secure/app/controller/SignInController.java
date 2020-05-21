package com.piorun.secure.app.controller;

import com.piorun.secure.app.model.Salt;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.repository.SaltRepository;
import com.piorun.secure.app.repository.UserRepository;
import com.piorun.secure.app.security.PasswordUtils;
import com.piorun.secure.app.security.verifiers.ParamsVerifier;
import com.piorun.secure.app.security.verifiers.UserVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignInController {


    private final ParamsVerifier verifier;
    private final UserVerifier userVerifier;
    private final SaltRepository saltRepository;
    private final UserRepository userRepository;

    @Autowired
    public SignInController(ParamsVerifier verifier, UserVerifier userVerifier, SaltRepository saltRepository, UserRepository userRepository) {
        this.verifier = verifier;
        this.userVerifier = userVerifier;
        this.saltRepository = saltRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<User> registerUser(String username, String password, String email) {
        verifier.verifyInputParameters(username, password, email);
        userVerifier.verify(username);
        String s = PasswordUtils.generateSalt();
        Salt salt = new Salt(s);
        String hash = PasswordUtils.hashPassword(password, s);
        Salt savedSalt = saltRepository.save(salt);
        User user = new User(username, hash, email, savedSalt.getId());
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

}