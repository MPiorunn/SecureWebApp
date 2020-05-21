package com.piorun.secure.app.controller;

import com.piorun.secure.app.exception.SignInException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController {


    @ExceptionHandler(SignInException.class)
    public ResponseEntity<String> handleIncorrectPassword(SignInException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
