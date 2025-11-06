package dev.berke.app.basket.api;

import dev.berke.app.basket.api.dto.BasketAddItemRequest;
import dev.berke.app.basket.api.dto.BasketResponse;
import dev.berke.app.basket.application.BasketService;
import dev.berke.app.basket.api.dto.BasketTotalPriceResponse;
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

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BasketResponse> getBasket(
            @AuthenticationPrincipal String customerIdPrincipal
    ){
        String customerId = customerIdPrincipal;

        BasketResponse basketResponse = basketService.getBasket(customerId);
        return new ResponseEntity<>(basketResponse, HttpStatus.OK);
    }

    @PostMapping("/items")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BasketResponse> addItemToBasket(
            @AuthenticationPrincipal String customerIdPrincipal,
            @RequestBody BasketAddItemRequest basketAddItemRequest
    ) {
        String customerId = customerIdPrincipal;

        BasketResponse basketResponse =
                basketService.addItemToBasket(customerId, basketAddItemRequest);

        return new ResponseEntity<>(basketResponse, HttpStatus.OK);
    }

    @GetMapping("/total-price")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BasketTotalPriceResponse> calculateTotalBasketPrice(
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        String customerId = customerIdPrincipal;

        BasketTotalPriceResponse totalPriceResponse =
                basketService.calculateTotalBasketPrice(customerId);

        return new ResponseEntity<>(totalPriceResponse, HttpStatus.OK);
    }
}
