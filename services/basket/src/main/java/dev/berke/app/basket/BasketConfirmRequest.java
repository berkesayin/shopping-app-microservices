package dev.berke.app.basket;

public record BasketConfirmRequest(
        Integer productId,
        Integer quantity) {
}
