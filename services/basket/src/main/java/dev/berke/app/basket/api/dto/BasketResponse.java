package dev.berke.app.basket.api.dto;

import dev.berke.app.basket.domain.model.BasketItem;

import java.util.List;

public record BasketResponse(
        String customerId,
        List<BasketItem> items
) {
}