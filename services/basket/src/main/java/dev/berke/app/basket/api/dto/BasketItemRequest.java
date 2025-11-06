package dev.berke.app.basket.api.dto;

public record BasketItemRequest(
        Integer productId,
        Integer quantity
) {
}
