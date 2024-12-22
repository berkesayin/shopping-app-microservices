package dev.berke.app.customer;

import dev.berke.app.constants.ValidationMessageConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        String id,

        @NotNull(message = ValidationMessageConstants.CUSTOMER_FIRSTNAME_REQUIRED)
        String firstname,

        @NotNull(message = ValidationMessageConstants.CUSTOMER_LASTNAME_REQUIRED)
        String lastname,

        @Email(message = ValidationMessageConstants.CUSTOMER_EMAIL_INVALID)
        String email,

        Address address
) {
}
