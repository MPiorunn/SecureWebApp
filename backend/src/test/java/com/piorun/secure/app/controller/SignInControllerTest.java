package com.piorun.secure.app.controller;

import com.piorun.secure.app.repository.SaltRepository;
import com.piorun.secure.app.repository.UserRepository;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


import static io.restassured.RestAssured.get;
import static io.restassured.module.webtestclient.RestAssuredWebTestClient.given;


public class SignInControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SaltRepository saltRepository;


    @Before
    public void setup(){

    }

    @Test
    public void contextLoads() {

//        given()
//                .standaloneSetup(new SignInController())

    }
}