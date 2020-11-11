package com.piorun.secure.app.security.verifiers;

import com.piorun.secure.app.exception.VerificationException;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.repository.UserRepository;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserVerifierTest {

    private final UserRepository repository = mock(UserRepository.class);
    private final UserVerifier verifier = new UserVerifier(repository);

    @Test(expected = VerificationException.class)
    public void shouldThrowExceptionIfUsernameTaken() {
        when(repository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        verifier.verifyIfUserExists("Some username", "Some email");
    }

    @Test(expected = VerificationException.class)
    public void shouldThrowExceptionIfEmailTaken() {
        when(repository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        verifier.verifyIfUserExists("Some username", "Some email");
    }

    @Test
    public void shouldPassIfUsernameAndEmailAvailable() {
        when(repository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        verifier.verifyIfUserExists("Some username", "Some email");
    }

}