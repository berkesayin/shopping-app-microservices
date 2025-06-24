package dev.berke.app.basket;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/baskets")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BasketResponse> getBasketByCustomerId(
            // @PathVariable("customerId") String customerId
            @AuthenticationPrincipal String customerIdPrincipal
    ){
        String customerId = customerIdPrincipal;

        BasketResponse basketResponse = basketService.getBasketByCustomerId(customerId);
        return new ResponseEntity<>(basketResponse, HttpStatus.OK);
    }

    @PostMapping("/items")
    public ResponseEntity<BasketResponse> addItemToBasket(
            @RequestBody BasketAddItemRequest basketAddItemRequest
    ) {
        BasketResponse basketResponse = basketService.addItemToBasket(basketAddItemRequest);
        return new ResponseEntity<>(basketResponse, HttpStatus.OK);
    }

    @GetMapping("/{customerId}/total-price")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BasketTotalPriceResponse> calculateTotalBasketPrice(
            // @PathVariable("customerId") String customerId
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        String customerId = customerIdPrincipal;

        BasketTotalPriceResponse totalPriceResponse =
                basketService.calculateTotalBasketPrice(customerId);

        return new ResponseEntity<>(totalPriceResponse, HttpStatus.OK);
    }
}
