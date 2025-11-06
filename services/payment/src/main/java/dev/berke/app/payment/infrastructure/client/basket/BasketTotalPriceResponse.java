package dev.berke.app.payment.infrastructure.client.basket;

import java.math.BigDecimal;

public record BasketTotalPriceResponse(
        String customerId,
        BigDecimal totalPrice
) {
}
