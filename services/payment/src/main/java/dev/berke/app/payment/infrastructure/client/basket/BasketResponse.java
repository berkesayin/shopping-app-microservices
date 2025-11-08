package dev.berke.app.payment.infrastructure.client.basket;

import java.util.List;

public record BasketResponse(
        String customerId,
        List<BasketItem> items
) {
}
