package dev.berke.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

// client -> gateway -> auth
public record SignUpRequest(
        @NotBlank
        String username,

        Set<String> role, // default ROLE_USER

        @NotBlank
        String password,

        // email, name, surname, gsmNumber are sent to customer service
        @NotBlank
        @Email
        String email,

        @NotBlank
        String name,

        @NotBlank
        String surname,

        @NotBlank
        String gsmNumber
) {
}