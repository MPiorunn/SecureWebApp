package com.piorun.secure.app.controller;

import com.piorun.secure.app.model.Salt;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.repository.SaltRepository;
import com.piorun.secure.app.repository.UserRepository;
import com.piorun.secure.app.security.verifiers.ParamsVerifier;
import com.piorun.secure.app.security.verifiers.UserVerifier;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static io.restassured.module.webtestclient.RestAssuredWebTestClient.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
public class SignInControllerTest {

    private final String SIGN_IN_PATH = "/add";
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String EMAIL = "email";

    private final UserRepository userRepository = mock(UserRepository.class);
    private final SaltRepository saltRepository = mock(SaltRepository.class);
    private final ParamsVerifier verifier = new ParamsVerifier();
    private final UserVerifier userVerifier = new UserVerifier(userRepository);
    private final Salt salt = mock(Salt.class);

    @Before
    public void setup() {
        String uuid = UUID.randomUUID().toString();
        when(salt.getId()).thenReturn(uuid);
        when(saltRepository.save(any())).thenReturn(salt);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    }

    @Test
    public void shouldReturn200OnCorrectParams() {

        given()
                .standaloneSetup(new SignInController(verifier, userVerifier, saltRepository, userRepository))
                .param(USERNAME, "NewUser123")
                .param(PASSWORD, "MyNewP@ssw0rD")
                .param(EMAIL, "jeff.fafa@dot.com")
                .when()
                .post(SIGN_IN_PATH)
                .then()
                .assertThat()
                .status(HttpStatus.OK);

    }

    @Test
    public void shouldReturn400OnWrongUsername() {

        given()
                .standaloneSetup(new SignInController(verifier, userVerifier, saltRepository, userRepository))
                .param(USERNAME, "short")
                .param(PASSWORD, "MyNewP@ssw0rD")
                .param(EMAIL, "jeff.fafa@dot.com")
                .when()
                .post(SIGN_IN_PATH)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

    }


    @Test
    public void shouldReturn400OnWrongPassword() {

        given()
                .standaloneSetup(new SignInController(verifier, userVerifier, saltRepository, userRepository))
                .param(USERNAME, "NewUser123")
                .param(PASSWORD, "wrong")
                .param(EMAIL, "jeff.fafa@dot.com")
                .when()
                .post(SIGN_IN_PATH)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void shouldReturn400OnWrongEmail() {

        given()
                .standaloneSetup(new SignInController(verifier, userVerifier, saltRepository, userRepository))
                .param(USERNAME, "NewUser123")
                .param(PASSWORD, "MyNewP@ssw0rD")
                .param(EMAIL, "incorrecto")
                .when()
                .post(SIGN_IN_PATH)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void shouldReturn400OnWhenUsernameTaken() {

        String username = "NewUser123";
        String email = "jeff.fafa@dot.com";
        when(userRepository.findByUsername(username)).thenThrow(IncorrectResultSizeDataAccessException.class);
        when(userRepository.findByUsername(email)).thenReturn(Optional.of(new User()));

        given()
                .standaloneSetup(new SignInController(verifier, userVerifier, saltRepository, userRepository))
                .param(USERNAME, username)
                .param(PASSWORD, "MyNewP@ssw0rD")
                .param(EMAIL, email)
                .when()
                .post(SIGN_IN_PATH)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void shouldReturn400OnWhenEmailTaken() {

        String username = "NewUser123";
        String email = "jeff.fafa@dot.com";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
        when(userRepository.findByUsername(email)).thenThrow(IncorrectResultSizeDataAccessException.class);

        given()
                .standaloneSetup(new SignInController(verifier, userVerifier, saltRepository, userRepository))
                .param(USERNAME, "NewUser123")
                .param(PASSWORD, "MyNewP@ssw0rD")
                .param(EMAIL, "jeff.fafa@dot.com")
                .when()
                .post(SIGN_IN_PATH)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

    }
}