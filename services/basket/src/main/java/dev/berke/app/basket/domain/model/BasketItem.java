package dev.berke.app.basket.domain.model;

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
    private BigDecimal basePrice;
    private String manufacturer;
    private Integer categoryId;
    private ItemType itemType;
    private Integer quantity;
}
