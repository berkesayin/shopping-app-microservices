package dev.berke.app.customer;

public record CustomerResponse (
        String id,
        String name,
        String surname,
        String gsmNumber,
        String email,
        String identityNumber,
        String registrationAddress,
        String city,
        String country,
        String zipCode
){
}
