package dev.berke.app.order.api.dto;

public record OrderResponse(
        Integer orderId,
        String reference
) {
}