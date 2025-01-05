package dev.berke.app.basket;

import java.math.BigDecimal;

public record BasketTotalPriceResponse(
        String customerId,
        BigDecimal totalPrice) {
}