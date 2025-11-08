package dev.berke.app.consumer.event;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductPublishedEvent(
        Integer productId,
        String productName,
        Integer categoryId,
        String categoryName,
        BigDecimal basePrice,
        BigDecimal minPrice,
        String manufacturer,
        String sku,
        Boolean status,
        Instant createdOn
) {
}