package dev.berke.app.events;

import dev.berke.app.payment.PaymentMethod;

import java.math.BigDecimal;

public record PaymentReceivedEvent(
        String customerName,
        String email,
        BigDecimal totalPrice,
        PaymentMethod paymentMethod
) {
}