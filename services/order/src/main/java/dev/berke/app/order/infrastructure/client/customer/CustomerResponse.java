package dev.berke.app.order.infrastructure.client.customer;

import java.util.List;

public record CustomerResponse(
        String id,
        String name,
        String surname,
        String email,
        String gsmNumber,
        List<Address> billingAddresses,
        List<Address> shippingAddresses,
        String activeBillingAddressId,
        String activeShippingAddressId
) {
}