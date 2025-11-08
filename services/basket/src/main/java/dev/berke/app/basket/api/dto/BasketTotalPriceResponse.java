package dev.berke.app.basket.api.dto;

import java.math.BigDecimal;

public record BasketTotalPriceResponse(
        String customerId,
        BigDecimal totalPrice
) {
}