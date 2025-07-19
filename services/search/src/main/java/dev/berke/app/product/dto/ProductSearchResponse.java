package dev.berke.app.product.dto;

import org.springframework.data.domain.Page;
import java.util.List;

public record ProductSearchResponse(
        List<ProductSearchResult> products,
        List<SearchAggregation> aggregations,
        long totalHits,
        int totalPages
) {
    // factory method to create a response from a Page of results and a list of aggregations
    public static ProductSearchResponse from(
            Page<ProductSearchResult> page,
            List<SearchAggregation> aggregations
    ) {
        return new ProductSearchResponse(
                page.getContent(),
                aggregations,
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}