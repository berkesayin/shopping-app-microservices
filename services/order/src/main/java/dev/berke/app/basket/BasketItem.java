package dev.berke.app.basket;

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
    private Double price;
    private Integer quantity;
}