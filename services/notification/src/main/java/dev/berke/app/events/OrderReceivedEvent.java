package dev.berke.app.events;

import dev.berke.app.basket.BasketItem;
import dev.berke.app.payment.PaymentMethod;

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