package dev.berke.app.kafka;

import dev.berke.app.order.PaymentMethod;

public record OrderConfirmation(
        String customerEmail,
        String customerId,
        String reference,
        PaymentMethod paymentMethod
) {
}