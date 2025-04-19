package dev.berke.app.kafka.payment;

import dev.berke.app.kafka.basket.BasketItem;

import java.math.BigDecimal;
import java.util.List;

public record PaymentConfirmationRequest(
        String name,
        String surname,
        String email,
        List<BasketItem> basketItems,
        BigDecimal totalBasketPrice
) {
}
