package dev.berke.app.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('BACKOFFICE')")
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody @Valid ProductRequest productRequest
    ) {
        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    @PatchMapping("/{productId}/status")
    @PreAuthorize("hasRole('BACKOFFICE')")
    public ResponseEntity<ProductResponse> setProductStatus(
            @PathVariable("productId") Integer productId,
            @RequestBody @Valid ProductStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(productService.setProductStatus(productId, request.status()));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable("productId") Integer productId
    ) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{productId}/category-id")
    @PreAuthorize("hasRole('BACKOFFICE')")
    public ResponseEntity<Integer> getCategoryIdOfProduct(
            @PathVariable("productId") Integer productId
    ) {
        return ResponseEntity.ok(productService.getCategoryIdOfProduct(productId));
    }

}
