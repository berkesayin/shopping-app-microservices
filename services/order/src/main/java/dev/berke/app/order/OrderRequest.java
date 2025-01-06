package dev.berke.app.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(

        @NotNull(message = "Customer id can not be null")
        String customerId,

        @NotBlank(message = "Reference can not be blank")
        String reference,

        PaymentMethod paymentMethod
) {
}
