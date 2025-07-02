package dev.berke.app.customer;

public record CustomerUpdateRequest(
        String name,
        String surname,
        String gsmNumber,
        String email,
        String password,
        String identityNumber,
        String registrationAddress
) {
}