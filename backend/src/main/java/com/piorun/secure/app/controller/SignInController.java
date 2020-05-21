package com.piorun.secure.app.controller;

import com.piorun.secure.app.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignInController {

    @PostMapping("/add")
    public ResponseEntity<User> registerUser(String username, String password, String email) {

        return null;
    }
}
