package dev.berke.app.basket;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/baskets")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @GetMapping("/{customer-id}")
    public ResponseEntity<BasketResponse> getBasketByCustomerId(
            @PathVariable("customer-id") String customerId
    ){
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

    @GetMapping("/{customer-id}/total-price")
    public ResponseEntity<BasketTotalPriceResponse> calculateTotalBasketPrice(
            @PathVariable("customer-id") String customerId
    ) {
        BasketTotalPriceResponse totalPriceResponse =
                basketService.calculateTotalBasketPrice(customerId);

        return new ResponseEntity<>(totalPriceResponse, HttpStatus.OK);
    }
}
