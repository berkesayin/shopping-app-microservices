package dev.berke.app.customer.api.dto;

import dev.berke.app.address.domain.model.Address;

import java.util.List;

public record CustomerResponse(
        String id,
        String name,
        String surname,
        String gsmNumber,
        String email,
        String password,
        String identityNumber,
        String registrationAddress,
        List<Address> billingAddresses,
        List<Address> shippingAddresses,
        String activeBillingAddressId,
        String activeShippingAddressId
) {
}
