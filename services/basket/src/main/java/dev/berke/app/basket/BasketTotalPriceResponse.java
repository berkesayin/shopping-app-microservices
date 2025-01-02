package dev.berke.app.basket;

public record BasketTotalPriceResponse(
        String customerId,
        Double totalPrice
) {
}