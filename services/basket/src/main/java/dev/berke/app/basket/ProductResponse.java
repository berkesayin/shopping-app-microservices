package dev.berke.app.basket;

import java.math.BigDecimal;

public record ProductResponse(
        Integer id,
        String name,
        Integer categoryId,
        ItemType itemType,
        BigDecimal price
) {
}
