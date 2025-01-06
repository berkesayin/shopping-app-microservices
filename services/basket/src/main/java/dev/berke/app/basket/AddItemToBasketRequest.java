package dev.berke.app.basket;

import java.util.List;

public record AddItemToBasketRequest(
        String customerId,
        List<BasketItemRequest> items
) {
}
