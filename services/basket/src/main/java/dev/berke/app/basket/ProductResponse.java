package dev.berke.app.basket;

public record ProductResponse(
        Integer id,
        String name,
        Integer categoryId,
        ItemType itemType,
        Double price
) {
}
