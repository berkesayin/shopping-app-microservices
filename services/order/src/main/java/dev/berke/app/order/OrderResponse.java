package dev.berke.app.order;

public record OrderResponse(
        Integer orderId,
        String reference
) {
}