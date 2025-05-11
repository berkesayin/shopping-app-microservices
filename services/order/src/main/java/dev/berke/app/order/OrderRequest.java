package dev.berke.app.order;

import dev.berke.app.payment.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotBlank(message = "Order reference number can not be blank")
        String reference,

        @NotNull(message = "Customer id can not be null")
        String customerId,

        PaymentMethod paymentMethod
) {
}
