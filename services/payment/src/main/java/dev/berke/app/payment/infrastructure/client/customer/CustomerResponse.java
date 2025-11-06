package dev.berke.app.payment.infrastructure.client.customer;

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
