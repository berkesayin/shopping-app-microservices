package dev.berke.app.product;

import dev.berke.app.constants.ProductConstants;
import jakarta.validation.constraints.NotNull;

public record ProductPurchaseRequest(

        @NotNull(message = ProductConstants.PRODUCT_ID_MANDATORY_MESSAGE)
        Integer productId,

        @NotNull(message = ProductConstants.QUANTITY_MANDATORY_MESSAGE)
        double quantity
) {
}
