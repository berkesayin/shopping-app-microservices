package dev.berke.app.basket.api.dto;

import java.util.List;

public record BasketAddItemRequest(
        List<BasketItemRequest> items
) {
}
