package dev.berke.app.product;

import dev.berke.app.constants.ProductConstants;
import dev.berke.app.exception.ProductPurchaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Integer createProduct(ProductRequest productRequest) {
        var product = productMapper.toProduct(productRequest);
        return productRepository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(
            List<ProductPurchaseRequest> productPurchaseRequest
    ) {

        // Extract the product IDs from the purchase requests
        var productIds = productPurchaseRequest
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        // Retrieve products from the database based on the extracted IDs
        var storedProducts = productRepository.findAllByIdInOrderById(productIds);

        // Check if all requested products exist in the database
        if (productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException(
                    ProductConstants.PRODUCT_NOT_FOUND_ERROR
            );
        }

        // Sort the purchase requests by product ID to match the order of stored products
        var storedRequest = productPurchaseRequest
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        // Initialize a list to hold the responses for purchased products
        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

        // Iterate through the stored products and corresponding purchase requests
        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i); // Get the current product
            var productRequest = storedRequest.get(i); // Get the corresponding purchase request

            // Check if there is enough quantity of the product available
            if (product.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException(
                        ProductConstants.INSUFFICIENT_STOCK_ERROR + productRequest.productId()
                );
            }

            // Calculate the new available quantity after the purchase
            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);

            // Save the updated product information to the database
            productRepository.save(product);

            // Create a toProductPurchaseResponse mapper and add it to the list of purchased products
            purchasedProducts.add(
                    productMapper.toProductPurchaseResponse(
                            product, productRequest.quantity()
                    )
            );
        }

        return purchasedProducts;
    }
}
