package com.piorun.secure.app.controller;

import com.piorun.secure.app.repository.SaltRepository;
import com.piorun.secure.app.repository.UserRepository;
import com.piorun.secure.app.security.verifiers.ParamsVerifier;
import com.piorun.secure.app.security.verifiers.UserVerifier;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import static io.restassured.module.webtestclient.RestAssuredWebTestClient.given;


public class SignInControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SaltRepository saltRepository;

    @Mock
    private ParamsVerifier verifier;

    @Mock
    private UserVerifier userVerifier;


    @Before
    public void setup() {

    }

    @Test
    public void contextLoads() {

        given()
                .standaloneSetup(new SignInController(verifier, userVerifier, saltRepository, userRepository))
                .when()
                .get("/")
                .then()
                .assertThat()
                .status(HttpStatus.OK);


    }
}