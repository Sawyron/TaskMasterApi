package org.sawyron.taskmaster.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sawyron.taskmaster.users.User;
import org.sawyron.taskmaster.users.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    void whenUserExists_thenReturnUserDetailsAdapter() {
        String username = "user";
        User user = new User();
        user.setId(UUID.fromString("bbcc4621-d88f-4a94-ae2f-b38072bf5087"));
        user.setPassword("password");
        user.setName(username);
        when(userRepository.findByName(eq(username))).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        verify(userRepository).findByName(username);
        verifyNoMoreInteractions(userRepository);
        assertInstanceOf(UserDetailsAdapter.class, userDetails);
        UserDetailsAdapter adapter = (UserDetailsAdapter) userDetails;
        assertAll(
                () -> assertEquals(user.getId(), adapter.getId()),
                () -> assertEquals(user.getName(), adapter.getUsername()),
                () -> assertEquals(user.getPassword(), adapter.getPassword())
        );
    }

    @Test
    void whenUserDoesNotExist_thenThrowUsernameNotFoundExceptionException() {
        String username = "user";
        when(userRepository.findByName(eq(username))).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
        verify(userRepository).findByName(username);
        verifyNoMoreInteractions(userRepository);
    }
}