package com.piorun.secure.app.controller;

import com.piorun.secure.app.exception.VerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController {


    @ExceptionHandler(VerificationException.class)
    public ResponseEntity<String> handleIncorrectPassword(VerificationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
