package org.sawyron.taskmaster.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sawyron.taskmaster.auth.dtos.RegisterRequest;
import org.sawyron.taskmaster.users.User;
import org.sawyron.taskmaster.users.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void whenUsernameDoesNotExists_thenRegisterUser() {
        when(userRepository.findByName(eq("user"))).thenReturn(Optional.empty());
        when(passwordEncoder.encode(eq("password"))).thenReturn("hash");
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        authService.register(new RegisterRequest("user", "password"));

        verify(userRepository).findByName("user");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(userArgumentCaptor.capture());
        User user = userArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals("user", user.getName()),
                () -> assertEquals("hash", user.getPassword())
        );
    }

    @Test
    void whenUserAlreadyExists_thenThrowRuntimeFoundException() {
        when(userRepository.findByName(eq("user"))).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () ->
                authService.register(new RegisterRequest("user", "")));

        verify(userRepository, times(1)).findByName("user");
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }
}