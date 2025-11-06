package dev.berke.app.payment.infrastructure.client.customer;

public record AddressResponse(
        String id,
        String contactName,
        String city,
        String country,
        String address,
        String zipCode,
        Boolean isActive
) {
}