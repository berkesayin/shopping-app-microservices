package dev.berke.app.kafka;

// import dev.berke.app.customer.CustomerResponse;
import dev.berke.app.order.PaymentMethod;

public record OrderConfirmationRequest(
        // CustomerResponse customer
        String customerEmail,
        String customerId,
        String reference,
        PaymentMethod paymentMethod
) {
}