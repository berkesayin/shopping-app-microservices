package dev.berke.app.customer;

public record AddressResponse(
        String id,
        String contactName,
        String city,
        String country,
        String address,
        String zipCode
) {
}
