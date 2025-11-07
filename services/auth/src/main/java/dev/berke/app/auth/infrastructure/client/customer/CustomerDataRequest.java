package dev.berke.app.auth.infrastructure.client.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerDataRequest(
        @NotBlank
        String name,

        @NotBlank
        String surname,

        @NotBlank
        String gsmNumber,

        @NotBlank
        @Email
        String email
) {}