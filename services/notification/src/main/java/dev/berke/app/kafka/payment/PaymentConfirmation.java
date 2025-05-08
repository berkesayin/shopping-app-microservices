package dev.berke.app.kafka.payment;

import java.math.BigDecimal;

public record PaymentConfirmation(

        String reference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerFirstname,
        String customerLastname,
        String customerEmail
) {
}
