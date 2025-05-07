package dev.berke.app.basket;

import java.util.List;

public record BasketAddItemRequest(
        String customerId,
        List<BasketItemRequest> items
) {
}
