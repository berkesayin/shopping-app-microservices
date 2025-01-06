package dev.berke.app.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record Customer(
        String id,

        @NotNull
        String name,

        @NotNull
        String surname,

        @NotNull
        @Email
        String email
) {
}