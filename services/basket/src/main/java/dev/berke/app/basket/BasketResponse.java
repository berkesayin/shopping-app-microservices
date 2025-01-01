package dev.berke.app.basket;

import java.util.List;

public record BasketResponse(
        String customerId,
        List<BasketItem> items
) {
}