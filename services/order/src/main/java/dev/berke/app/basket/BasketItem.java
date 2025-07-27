package dev.berke.app.basket;

import java.math.BigDecimal;

public record BasketItem(
        Integer productId,
        String productName,
        String manufacturer,
        Integer categoryId,
        ItemType itemType,
        BigDecimal basePrice,
        Integer quantity
) {
}