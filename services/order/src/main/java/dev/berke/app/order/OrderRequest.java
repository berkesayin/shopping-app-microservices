package dev.berke.app.order;


/*
public record OrderRequest(

        Integer id,

        @NotNull(message = OrderConstants.CUSTOMER_ID_NOT_NULL_MESSAGE)
        @NotEmpty(message = OrderConstants.CUSTOMER_ID_NOT_NULL_MESSAGE)
        @NotBlank(message = OrderConstants.CUSTOMER_ID_NOT_NULL_MESSAGE)
        String customerId,

        String reference,

        @Positive(message = OrderConstants.ORDER_AMOUNT_POSITIVE_MESSAGE)
        BigDecimal amount,

        @NotNull(message = OrderConstants.PAYMENT_METHOD_NOT_NULL_MESSAGE)
        PaymentMethod paymentMethod,

        List<BasketItem> basketItems

        // @NotEmpty(message = OrderConstants.PRODUCTS_NOT_EMPTY_MESSAGE)
        // List<PurchaseRequest> products


) {
}
*/

import dev.berke.app.basket.BasketItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(

        Integer id,

        @NotNull(message = "Customer id can not be null")
        String customerId,

        @NotBlank(message = "Reference can not be blank")
        String reference,

        // @NotNull(message = "Amount can not be null")
        BigDecimal amount,

        @NotBlank(message = "Payment method can not be blank")
        PaymentMethod paymentMethod,

        List<BasketItem> basketItems

) {
}
