package dev.berke.app.customer;

import dev.berke.app.constants.CustomerConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        String id,

        @NotNull(message = CustomerConstants.CUSTOMER_FIRSTNAME_REQUIRED)
        String firstname,

        @NotNull(message = CustomerConstants.CUSTOMER_LASTNAME_REQUIRED)
        String lastname,

        @Email(message = CustomerConstants.CUSTOMER_EMAIL_INVALID)
        String email,

        Address address
) {
}
