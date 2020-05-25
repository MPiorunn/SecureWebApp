package com.piorun.secure.app.controller;

import com.piorun.secure.app.model.Salt;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.repository.SaltRepository;
import com.piorun.secure.app.repository.UserRepository;
import com.piorun.secure.app.security.PasswordUtils;
import com.piorun.secure.app.security.verifiers.ParamsVerifier;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static io.restassured.module.webtestclient.RestAssuredWebTestClient.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginControllerTest {

    private final String LOGIN_PATH = "/login";
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String SALT = PasswordUtils.generateSalt();
    private final String SALT_ID = UUID.randomUUID().toString();
    private final UserRepository userRepository = mock(UserRepository.class);
    private final SaltRepository saltRepository = mock(SaltRepository.class);
    private final ParamsVerifier verifier = new ParamsVerifier();
    private final Salt salt = mock(Salt.class);


    @Before
    public void setup() {
        when(salt.getValue()).thenReturn(SALT);
        when(saltRepository.findById(SALT_ID)).thenReturn(Optional.of(salt));
    }

    @Test
    public void shouldReturn200WhenInputOk() {
        String username = "NewUser123";
        String password = "MyNewP@ssw0rD";
        String hash = PasswordUtils.hashPassword(password, SALT);
        User user = new User(username, hash, null, SALT_ID);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        given()
                .standaloneSetup(new LoginController(verifier, saltRepository, userRepository))
                .param(USERNAME, username)
                .param(PASSWORD, password)
                .when()
                .post(LOGIN_PATH)
                .then()
                .assertThat()
                .status(HttpStatus.OK);

    }


    @Test
    public void shouldReturn400WhenUsernameTooShort() {
        String username = "s";
        String password = "MyNewP@ssw0rD";
        String hash = PasswordUtils.hashPassword(password, SALT);
        User user = new User(username, hash, null, SALT_ID);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        given()
                .standaloneSetup(new LoginController(verifier, saltRepository, userRepository))
                .param(USERNAME, username)
                .param(PASSWORD, password)
                .when()
                .post(LOGIN_PATH)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);
    }


    @Test
    public void shouldReturn400WhenUserNotFound() {
        String username = "NewUser123";
        String password = "MyNewP@ssw0rD";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        given()
                .standaloneSetup(new LoginController(verifier, saltRepository, userRepository))
                .param(USERNAME, username)
                .param(PASSWORD, password)
                .when()
                .post(LOGIN_PATH)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);
    }


    @Test
    public void shouldReturn400WhenPasswordNotValid() {
        String username = "NewUser123";
        String password = "dsa";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        given()
                .standaloneSetup(new LoginController(verifier, saltRepository, userRepository))
                .param(USERNAME, username)
                .param(PASSWORD, password)
                .when()
                .post(LOGIN_PATH)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);
    }


    @Test
    public void shouldReturn400WhenWrongPassword() {
        String username = "NewUser123";
        String password = "MyNewP@ssw0rD";
        String hash = PasswordUtils.hashPassword(password, SALT);
        User user = new User(username, "SOME HASH", null, SALT_ID);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        given()
                .standaloneSetup(new LoginController(verifier, saltRepository, userRepository))
                .param(USERNAME, username)
                .param(PASSWORD, password)
                .when()
                .post(LOGIN_PATH)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);
    }

}