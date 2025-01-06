package dev.berke.app.address;

public record AddressResponse(
        String contactName,
        String city,
        String country,
        String address,
        String zipCode
) {
}
