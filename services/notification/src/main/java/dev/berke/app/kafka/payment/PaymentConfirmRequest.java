package dev.berke.app.kafka.payment;

import java.math.BigDecimal;

public record PaymentConfirmRequest(
        String customerName,
        String email,
        BigDecimal totalPrice,
        PaymentMethod paymentMethod
) {
}
