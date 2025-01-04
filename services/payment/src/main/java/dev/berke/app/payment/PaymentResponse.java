package dev.berke.app.payment;

public record PaymentResponse(
        String status,
        String paymentId
) {
}