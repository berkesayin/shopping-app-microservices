package dev.berke.app.order;

import dev.berke.app.basket.BasketItemRequest;
import dev.berke.app.constants.OrderConstants;
import dev.berke.app.product.PurchaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(

        Integer id,
        String reference,

        @Positive(message = OrderConstants.ORDER_AMOUNT_POSITIVE_MESSAGE)
        BigDecimal amount,

        @NotNull(message = OrderConstants.PAYMENT_METHOD_NOT_NULL_MESSAGE)
        PaymentMethod paymentMethod,

        @NotNull(message = OrderConstants.CUSTOMER_ID_NOT_NULL_MESSAGE)
        @NotEmpty(message = OrderConstants.CUSTOMER_ID_NOT_NULL_MESSAGE)
        @NotBlank(message = OrderConstants.CUSTOMER_ID_NOT_NULL_MESSAGE)
        String customerId,

        // @NotEmpty(message = OrderConstants.PRODUCTS_NOT_EMPTY_MESSAGE)
        List<PurchaseRequest> products,

        List<BasketItemRequest> basketItemRequests
) {
}
