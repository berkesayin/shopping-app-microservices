package dev.berke.app.customer;

import dev.berke.app.address.Address;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CustomerRequest (
        String id,

        @NotNull
        String name,

        @NotNull
        String surname,

        @NotNull
        String gsmNumber,

        @NotNull
        String email,

        @NotNull
        String password, // added field

        @NotNull
        String identityNumber,

        @NotNull
        String registrationAddress,

        @NotNull
        String city,

        @NotNull
        String country,

        @NotNull
        String zipCode,

        List<Address> billingAddresses,
        List<Address> shippingAddresses,
        String activeBillingAddressId, // updated field
        String activeShippingAddressId // updated field
){
}