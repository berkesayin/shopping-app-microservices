package dev.berke.app.basket;

import java.util.List;

public record BasketRequest(
        String customerId,
        List<BasketItemRequest> items
) {
}
