package dev.berke.app.productsearch.api.dto;

import dev.berke.app.productsearch.domain.document.ProductDocument;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductSearchResult(
        Integer productId,
        String productName,
        CategorySummary category,
        BigDecimal basePrice,
        BigDecimal minPrice,
        String manufacturer,
        String sku,
        Instant createdOn
) {
    public record CategorySummary(String id, String name) {}

    // factory method to convert a ProductDocument to a ProductSearchResult
    public static ProductSearchResult from(ProductDocument doc) {
        return new ProductSearchResult(
                doc.getProductId(),
                doc.getProductName(),
                new CategorySummary(doc.getCategory().getId(), doc.getCategory().getName()),
                doc.getBasePrice(),
                doc.getMinPrice(),
                doc.getManufacturer(),
                doc.getSku(),
                doc.getCreatedOn()
        );
    }
}