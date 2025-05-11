package dev.berke.app.kafka;

import dev.berke.app.payment.PaymentMethod;

import java.math.BigDecimal;

public record PaymentConfirmRequest(
        String customerName,
        String email,
        BigDecimal totalPrice,
        PaymentMethod paymentMethod
) {
}