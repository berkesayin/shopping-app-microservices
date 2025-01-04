package dev.berke.app.kafka;

import dev.berke.app.basket.BasketItem;
import dev.berke.app.customer.CustomerResponse;
import dev.berke.app.order.PaymentMethod;

import java.util.List;

public record OrderConfirmation(
        String orderReference,
        Double totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<BasketItem> basketItems
) {
}
