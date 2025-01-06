package dev.berke.app.customer;

public record CustomerResponse(
        String id,
        String name,
        String surname,
        String email
) {
}