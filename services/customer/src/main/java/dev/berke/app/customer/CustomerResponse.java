package dev.berke.app.customer;

import dev.berke.app.address.Address;
import java.util.List;

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
        List<Address> billingAddresses,
        List<Address> shippingAddresses,
        String activeBillingAddressId, // updated field
        String activeShippingAddressId // updated field
) {
}
