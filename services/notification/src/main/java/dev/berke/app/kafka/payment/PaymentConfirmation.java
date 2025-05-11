package dev.berke.app.kafka.payment;

import java.math.BigDecimal;

public record PaymentConfirmation(
        String customerName,
        String email,
        BigDecimal totalPrice,
        PaymentMethod paymentMethod
) {
}
