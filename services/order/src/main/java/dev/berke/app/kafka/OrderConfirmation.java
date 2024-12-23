package dev.berke.app.kafka;

import dev.berke.app.customer.CustomerResponse;
import dev.berke.app.order.PaymentMethod;
import dev.berke.app.product.PurchaseResponse;
import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
