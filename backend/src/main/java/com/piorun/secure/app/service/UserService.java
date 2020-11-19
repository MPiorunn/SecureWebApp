package com.piorun.secure.app.service;

import com.piorun.secure.app.exception.VerificationException;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.repository.UserRepository;
import com.piorun.secure.app.security.verifiers.EmailFormatVerifier;
import com.piorun.secure.app.security.verifiers.UsernameFormatVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final EmailFormatVerifier emailFormatVerifier;
    private final UsernameFormatVerifier usernameFormatVerifier;

    public Optional<User> findByEmail(String email) {
        try {
            emailFormatVerifier.verify(email);
        } catch (VerificationException e) {
            log.info("Provided email was not in correct format");
            return Optional.empty();
        }
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        try {
            usernameFormatVerifier.verify(username);
        } catch (VerificationException e) {
            log.info("Provided email was not in correct format");
            return Optional.empty();
        }
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }
}
