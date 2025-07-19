package dev.berke.app.product.dto;

import java.util.List;

public record SearchAggregation(
        String name,
        List<Bucket> buckets
) {
    public record Bucket(String key, long count) {}
}