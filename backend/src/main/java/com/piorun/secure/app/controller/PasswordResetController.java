package com.piorun.secure.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordResetController {


    @PostMapping
    public ResponseEntity<String> resetPassword() {
        return null;
    }
}
