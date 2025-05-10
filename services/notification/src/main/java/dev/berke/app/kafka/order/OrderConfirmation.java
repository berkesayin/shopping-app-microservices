package dev.berke.app.kafka.order;

import dev.berke.app.kafka.payment.PaymentMethod;

public record OrderConfirmation(
        String customerEmail,
        String customerId,
        String reference,
        PaymentMethod paymentMethod
) {
}