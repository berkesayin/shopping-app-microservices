package dev.berke.app.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotBlank(message = "Reference can not be blank")
        String reference,

        @NotNull(message = "Customer id can not be null")
        String customerId,

        @NotNull(message = "Customer email can not be null")
        String customerEmail,

        PaymentMethod paymentMethod
) {
}
