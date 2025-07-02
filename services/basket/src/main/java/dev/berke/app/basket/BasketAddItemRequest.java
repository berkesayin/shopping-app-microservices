package dev.berke.app.basket;

import java.util.List;

public record BasketAddItemRequest(
        List<BasketItemRequest> items
) {
}
