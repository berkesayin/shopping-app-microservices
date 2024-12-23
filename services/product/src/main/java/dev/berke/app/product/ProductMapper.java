package dev.berke.app.product;

import dev.berke.app.category.Category;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

    public Product toProduct(ProductRequest productRequest) {
        return Product.builder()
                .id(productRequest.id())
                .name(productRequest.name())
                .description(productRequest.description())
                .availableQuantity(productRequest.availableQuantity())
                .price(productRequest.price())
                .category(
                        Category.builder()
                                .id(productRequest.categoryId())
                                .build()
                )
                .build();
    }

    public ProductPurchaseResponse toProductPurchaseResponse(
            Product product, double quantity
    ) {
        return new ProductPurchaseResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity
        );
    }
}
