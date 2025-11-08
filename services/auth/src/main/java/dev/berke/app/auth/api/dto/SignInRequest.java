package dev.berke.app.auth.api.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank
        String username,

        @NotBlank
        String password
){
}

