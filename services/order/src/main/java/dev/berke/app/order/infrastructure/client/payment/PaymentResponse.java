package dev.berke.app.order.infrastructure.client.payment;

public record PaymentResponse(
        String status,
        String paymentId
) {
}
