package dev.berke.app.customer;

import dev.berke.app.address.Address;
import jakarta.validation.constraints.NotNull;

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
        String identityNumber,

        @NotNull
        String registrationAddress,

        @NotNull
        String city,

        @NotNull
        String country,

        @NotNull
        String zipCode,

        Address address
){
}