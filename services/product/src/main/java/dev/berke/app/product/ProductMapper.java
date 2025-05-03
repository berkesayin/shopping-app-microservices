package dev.berke.app.product;

import dev.berke.app.category.Category;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

    public Product toProduct(ProductRequest productRequest) {
        return Product.builder()
                .productName(productRequest.productName())
                .basePrice(productRequest.basePrice())
                .minPrice(productRequest.minPrice())
                .manufacturer(productRequest.manufacturer())
                .sku(productRequest.sku())
                .createdOn(productRequest.createdOn())
                .status(productRequest.status())
                .category(
                        Category.builder()
                                .categoryId(productRequest.categoryId())
                                .build())
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getProductId(),
                product.getProductName(),
                product.getBasePrice(),
                product.getMinPrice(),
                product.getManufacturer(),
                product.getSku(),
                product.getCreatedOn(),
                product.getStatus(),
                product.getCategory().getCategoryId()
        );
    }
}
