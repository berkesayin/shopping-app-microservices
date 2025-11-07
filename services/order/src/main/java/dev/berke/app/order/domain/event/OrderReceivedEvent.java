package dev.berke.app.order.domain.event;

import dev.berke.app.order.infrastructure.client.basket.BasketItem;
import dev.berke.app.order.infrastructure.client.payment.PaymentMethod;

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