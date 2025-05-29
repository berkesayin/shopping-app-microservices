package dev.berke.auth.controller;

import dev.berke.auth.dto.SignInResponse;
import dev.berke.auth.dto.MessageResponse;
import dev.berke.auth.dto.SignInRequest;
import dev.berke.auth.dto.SignUpRequest;
import dev.berke.auth.entity.User;
import dev.berke.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    // auth methods: sign in, sign up, logout

    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> getToken(
            @Valid @RequestBody SignInRequest signInRequest
    ) {
        SignInResponse signInResponse = authService.getToken(signInRequest);
        return ResponseEntity.ok(signInResponse);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        User registeredUser = authService.createUser(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully! Email: "
                + registeredUser.getEmail()));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> invalidateToken(
            @RequestHeader("Authorization") String tokenHeader
    ) {
        try {
            authService.invalidateToken(tokenHeader);
            return ResponseEntity.ok(new MessageResponse("Logout successful. Token invalidated."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        } catch (RuntimeException e) { // for Redis errors or other runtime issues
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error during logout: " + e.getMessage()));
        } catch (Exception e) { // all unexpected errors
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Unexpected error! " + e.getMessage()));
        }
    }
}