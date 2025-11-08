package dev.berke.app.basket.infrastructure.client.product;

import dev.berke.app.basket.domain.model.ItemType;

import java.math.BigDecimal;

public record ProductResponse(
        Integer productId,
        String productName,
        BigDecimal basePrice,
        String manufacturer,
        Integer categoryId,
        ItemType itemType
) {
}