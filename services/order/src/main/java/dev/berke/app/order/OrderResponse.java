package dev.berke.app.order;

import java.math.BigDecimal;

public record OrderResponse(
        Integer orderId,
        String reference
) {
}