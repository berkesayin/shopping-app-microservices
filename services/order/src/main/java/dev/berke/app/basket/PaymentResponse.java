package dev.berke.app.basket;

public record PaymentResponse(
        String status,
        String paymentId
) {
}