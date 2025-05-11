package dev.berke.app.kafka.basket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketItem {

    private Integer productId;
    private String productName;
    private Integer categoryId;
    private ItemType itemType;
    private BigDecimal basePrice;
    private Integer quantity;
}