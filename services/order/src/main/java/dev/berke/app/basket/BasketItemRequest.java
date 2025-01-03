package dev.berke.app.basket;

public record BasketItemRequest(
        Integer productId,
        Integer quantity
) {
}