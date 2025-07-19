package dev.berke.app.product.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductSearchRequest(
        String query,
        List<String> categories,
        List<String> manufacturers,
        PriceRange priceRange,
        SortCriteria sortBy,
        int page,
        int size
) {
    public record PriceRange(BigDecimal min, BigDecimal max) {}

    public enum SortCriteria { RELEVANCE, PRICE_ASC, PRICE_DESC, NEWEST }
}