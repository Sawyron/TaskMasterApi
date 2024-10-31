package org.sawyron.taskmaster.users;

import org.sawyron.taskmaster.auth.UserDetailsAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @GetMapping("greeting")
    public ResponseEntity<String> greet(@AuthenticationPrincipal UserDetailsAdapter userDetails) {
        return new ResponseEntity<>("Hello, user with id %s".formatted(userDetails.getId()), HttpStatus.OK);
    }
}
