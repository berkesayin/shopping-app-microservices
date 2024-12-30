package dev.berke.app.address;

import jakarta.validation.constraints.NotNull;

public record AddressRequest(
        // String id,

        @NotNull
        String contactName,

        @NotNull
        String city,

        @NotNull
        String country,

        @NotNull
        String address,

        @NotNull
        String zipCode
) {
}