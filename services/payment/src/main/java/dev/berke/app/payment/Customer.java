package dev.berke.app.payment;

import dev.berke.app.constants.PaymentConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record Customer(

        String id,

        @NotNull(message = PaymentConstants.FIRSTNAME_NOT_NULL_MESSAGE)
        String firstname,

        @NotNull(message = PaymentConstants.LASTNAME_NOT_NULL_MESSAGE)
        String lastname,

        @NotNull(message = PaymentConstants.EMAIL_NOT_NULL_MESSAGE)
        @Email(message = PaymentConstants.EMAIL_INVALID_FORMAT_MESSAGE)
        String email
) {
}
