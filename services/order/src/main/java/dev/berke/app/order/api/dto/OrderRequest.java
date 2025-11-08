package dev.berke.app.order.api.dto;

import dev.berke.app.order.infrastructure.client.payment.PaymentMethod;
import jakarta.validation.constraints.NotBlank;

public record OrderRequest(
        @NotBlank(message = "Order reference number can not be blank")
        String reference,

        PaymentMethod paymentMethod
) {
}
