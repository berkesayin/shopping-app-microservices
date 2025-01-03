package dev.berke.app.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Integer> createProduct(
            @RequestBody @Valid ProductRequest productRequest
    ) {
        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(
            @RequestBody List<ProductPurchaseRequest> productPurchaseRequest
    ) {
        return ResponseEntity.ok(productService.purchaseProducts(productPurchaseRequest));
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable("product-id") Integer productId
    ) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{product-id}/category-id")
    public ResponseEntity<Integer> getCategoryIdOfProduct(
            @PathVariable("product-id") Integer productId
    ) {
        return ResponseEntity.ok(productService.getCategoryIdOfProduct(productId));
    }

    @GetMapping("/{product-id}/available-quantity")
    public ResponseEntity<Double> getAvailableQuantityByProductId(
            @PathVariable("product-id") Integer productId
    ) {
        return ResponseEntity.ok(productService.getAvailableQuantityByProductId(productId));
    }


}
