package dev.berke.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record SignUpRequest(
        @NotBlank
        String username,

        @NotBlank
        @Email
        String email,

        Set<String> role,

        @NotBlank
        String password
) {
}