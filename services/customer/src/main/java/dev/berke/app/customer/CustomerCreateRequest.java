package dev.berke.app.customer;

import jakarta.validation.constraints.NotNull;

public record CustomerCreateRequest(
        String id,

        @NotNull 
        String name,

        @NotNull 
        String surname,

        @NotNull 
        String gsmNumber,

        @NotNull 
        String email,

        @NotNull 
        String password
) {}