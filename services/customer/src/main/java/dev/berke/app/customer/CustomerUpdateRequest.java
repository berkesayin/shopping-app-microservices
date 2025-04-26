package dev.berke.app.customer;

public record CustomerUpdateRequest(
    String id,
    String name,
    String surname,
    String gsmNumber,
    String email,
    String password,
    String identityNumber,
    String registrationAddress
) {}
