package dev.berke.app.product;

import dev.berke.app.constants.ProductConstants;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        Integer id,

        @NotNull(message = ProductConstants.PRODUCT_NAME_REQUIRED)
        String name,

        @NotNull(message = ProductConstants.PRODUCT_DESCRIPTION_REQUIRED)
        String description,

        @Positive(message = ProductConstants.AVAILABLE_QUANTITY_POSITIVE)
        Integer availableQuantity,

        @Positive(message = ProductConstants.PRICE_POSITIVE)
        BigDecimal price,

        @NotNull(message = ProductConstants.PRODUCT_CATEGORY_REQUIRED)
        Integer categoryId
) {
}