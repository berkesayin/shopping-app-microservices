package dev.berke.app.kafka;

import dev.berke.app.basket.BasketItem;
import dev.berke.app.payment.PaymentMethod;

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