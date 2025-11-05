package dev.berke.app.orderline;

public record OrderlineRequest(
        Integer id,
        Integer orderId,
        Integer productId,
        Integer quantity
) {
}
