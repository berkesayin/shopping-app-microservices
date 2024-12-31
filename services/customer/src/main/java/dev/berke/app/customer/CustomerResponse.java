package dev.berke.app.customer;

import dev.berke.app.address.Address;

public record CustomerResponse(
                String id,
                String name,
                String surname,
                String gsmNumber,
                String email,
                String identityNumber,
                String registrationAddress,
                String city,
                String country,
                String zipCode,
                Address billingAddress,
                Address shippingAddress) {
}