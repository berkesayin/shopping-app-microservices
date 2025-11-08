package dev.berke.app.product.domain.event;

public record ProductUnpublishedEvent(
        Integer productId
) {
}