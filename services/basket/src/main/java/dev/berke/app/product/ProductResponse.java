package dev.berke.app.product;

import dev.berke.app.basket.ItemType;

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
