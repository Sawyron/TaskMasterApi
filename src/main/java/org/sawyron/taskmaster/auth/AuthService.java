package org.sawyron.taskmaster.auth;

import org.sawyron.taskmaster.auth.dtos.RegisterRequest;
import org.sawyron.taskmaster.users.User;
import org.sawyron.taskmaster.users.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {
        if (userRepository.findByName(request.username()).isPresent()) {
            throw new RuntimeException("User with name '%s' already exists".formatted(request.username()));
        }
        var user = new User();
        user.setName(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }
}
