package dev.berke.app.basket;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketItem {

    private Integer productId;
    private String productName;
    private Integer categoryId;
    private ItemType itemType;
    private BigDecimal price;
    private Integer quantity;
}
