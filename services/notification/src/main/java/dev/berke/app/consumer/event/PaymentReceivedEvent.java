package dev.berke.app.consumer.event;

import dev.berke.app.consumer.model.PaymentMethod;

import java.math.BigDecimal;

public record PaymentReceivedEvent(
        String customerName,
        String email,
        BigDecimal totalPrice,
        PaymentMethod paymentMethod
) {
}
