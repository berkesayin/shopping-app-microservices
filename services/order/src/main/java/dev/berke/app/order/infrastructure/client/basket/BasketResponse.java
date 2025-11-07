package dev.berke.app.order.infrastructure.client.basket;

import java.util.List;

public record BasketResponse(
        String customerId,
        List<BasketItem> items
) {
}
