package dev.berke.app.product.application.mapper;

import dev.berke.app.product.domain.model.Category;
import dev.berke.app.product.domain.model.Product;
import dev.berke.app.product.api.dto.ProductRequest;
import dev.berke.app.product.api.dto.ProductResponse;
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
                .status(false)
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
