package dev.berke.app.consumer.event;

import dev.berke.app.consumer.model.BasketItem;
import dev.berke.app.consumer.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderReceivedEvent(
        String customerName,
        String customerEmail,
        String reference,
        PaymentMethod paymentMethod,
        List<BasketItem> productsToPurchase,
        BigDecimal totalPrice
) {
}