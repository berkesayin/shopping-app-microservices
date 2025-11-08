package dev.berke.app.order.infrastructure.client.basket;

import java.math.BigDecimal;

public record BasketTotalPriceResponse(
        String customerId,
        BigDecimal totalPrice) {
}
