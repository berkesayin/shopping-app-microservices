package dev.berke.app.kafka.order;

import dev.berke.app.kafka.basket.BasketItem;
import dev.berke.app.kafka.payment.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmRequest(
        String customerName,
        String customerEmail,
        String reference,
        PaymentMethod paymentMethod,
        List<BasketItem> productsToPurchase,
        BigDecimal totalPrice
) {
}