package dev.berke.app.product.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductRequest(
        String productName,
        BigDecimal basePrice,
        BigDecimal minPrice,
        String manufacturer,
        String sku,
        Instant createdOn,
        Integer categoryId
) {
}