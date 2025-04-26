package dev.berke.app.address;

import jakarta.validation.constraints.NotNull;

public record AddressRequest(

        @NotNull
        String contactName,

        @NotNull
        String city,

        @NotNull
        String country,

        @NotNull
        String address,

        @NotNull
        String zipCode,

        // New field with default false from client side
        Boolean isActive  // expected to be false on creation
) {
}