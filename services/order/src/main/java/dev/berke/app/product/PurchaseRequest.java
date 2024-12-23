package dev.berke.app.product;

import dev.berke.app.constants.OrderConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaseRequest(
        @NotNull(message = OrderConstants.PRODUCT_ID_NOT_NULL_MESSAGE)
        Integer productId,

        @Positive(message = OrderConstants.QUANTITY_POSITIVE_MESSAGE)
        double quantity
) {
}
