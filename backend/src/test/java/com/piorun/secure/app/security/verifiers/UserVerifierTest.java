package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.VerificationException;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.service.UserService;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserVerifierTest {

    private final UserService userService = mock(UserService.class);
    private final UserVerifier verifier = new UserVerifier(userService);

    @Test(expected = VerificationException.class)
    public void shouldThrowExceptionIfUsernameTaken() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        verifier.verifyIfUserExists("Some username", "Some email");
    }

    @Test(expected = VerificationException.class)
    public void shouldThrowExceptionIfEmailTaken() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        verifier.verifyIfUserExists("Some username", "Some email");
    }

    @Test
    public void shouldPassIfUsernameAndEmailAvailable() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

        verifier.verifyIfUserExists("Some username", "Some email");
    }

}