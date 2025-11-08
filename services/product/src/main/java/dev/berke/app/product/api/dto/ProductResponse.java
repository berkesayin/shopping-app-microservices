package dev.berke.app.product.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Integer productId,
        String productName,
        BigDecimal basePrice,
        BigDecimal minPrice,
        String manufacturer,
        String sku,
        Instant createdOn,
        Boolean status,
        Integer categoryId
) {
}
