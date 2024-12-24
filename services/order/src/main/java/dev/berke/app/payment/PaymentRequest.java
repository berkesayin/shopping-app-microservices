package dev.berke.app.payment;

import dev.berke.app.customer.CustomerResponse;
import dev.berke.app.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(

        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}