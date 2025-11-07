package dev.berke.app.order.infrastructure.client.customer;

public record Address(
        String id,
        String contactName,
        String city,
        String country,
        String address,
        String zipCode
) {
}
