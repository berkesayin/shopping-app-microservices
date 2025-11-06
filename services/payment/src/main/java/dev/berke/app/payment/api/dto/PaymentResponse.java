package dev.berke.app.payment.api.dto;

public record PaymentResponse(
        String status,
        String paymentId
) {
}