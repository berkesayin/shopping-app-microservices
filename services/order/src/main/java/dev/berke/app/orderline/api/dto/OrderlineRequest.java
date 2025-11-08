package dev.berke.app.orderline.api.dto;

public record OrderlineRequest(
        Integer id,
        Integer orderId,
        Integer productId,
        Integer quantity
) {
}
