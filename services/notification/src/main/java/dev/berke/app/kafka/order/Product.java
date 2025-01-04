package dev.berke.app.kafka.order;

import java.math.BigDecimal;

public record Product(

                Integer productId,
                String name,
                String description,
                BigDecimal price,
                Double quantity) {
}
