package dev.berke.app.auth.api;

import dev.berke.app.auth.api.dto.SignInResponse;
import dev.berke.app.auth.api.dto.MessageResponse;
import dev.berke.app.auth.api.dto.SignInRequest;
import dev.berke.app.auth.api.dto.SignUpRequest;
import dev.berke.app.user.domain.model.User;
import dev.berke.app.auth.application.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    // auth methods: sign in, sign up, logout

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> getToken(
            @Valid @RequestBody SignInRequest signInRequest
    ) {
        return ResponseEntity.ok(authService.getToken(signInRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        User registeredUser = authService.createUser(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully! Email: "
                + registeredUser.getEmail()));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> invalidateToken(
            @RequestHeader("Authorization") String tokenHeader
    ) {
        authService.invalidateToken(tokenHeader);
        return ResponseEntity.ok(new MessageResponse("Logout successful. Token invalidated."));
    }
}