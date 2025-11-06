package dev.berke.app.productsearch.api;

import dev.berke.app.productsearch.api.dto.AutocompleteSuggestionResponse;
import dev.berke.app.productsearch.api.dto.ProductSearchRequest;
import dev.berke.app.productsearch.api.dto.ProductSearchResponse;
import dev.berke.app.productsearch.application.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search/products")
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @PostMapping
    public ResponseEntity<ProductSearchResponse> searchProducts(
            @RequestBody ProductSearchRequest request
    ) {
        ProductSearchResponse response = productSearchService.searchProducts(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<AutocompleteSuggestionResponse> autocomplete(
            @RequestParam("query") String query
    ) {
        if (query == null || query.isBlank() || query.length() < 2) {
            return ResponseEntity.badRequest().build();
        }
        AutocompleteSuggestionResponse response = productSearchService.getAutocompleteSuggestions(query);
        return ResponseEntity.ok(response);
    }
}