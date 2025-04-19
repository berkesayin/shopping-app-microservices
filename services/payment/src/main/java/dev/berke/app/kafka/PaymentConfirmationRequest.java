package dev.berke.app.kafka;

import com.iyzipay.model.BasketItem;

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