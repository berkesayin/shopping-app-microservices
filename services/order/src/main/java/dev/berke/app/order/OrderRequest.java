package dev.berke.app.order;

import dev.berke.app.payment.PaymentMethod;
import jakarta.validation.constraints.NotBlank;

public record OrderRequest(
        @NotBlank(message = "Order reference number can not be blank")
        String reference,

        PaymentMethod paymentMethod
) {
}
