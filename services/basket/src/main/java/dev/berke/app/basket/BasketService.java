package dev.berke.app.basket;

import dev.berke.app.product.ProductClient;
import dev.berke.app.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductClient productClient;

    public BasketResponse getBasket(String customerId) {
        Optional<Basket> basket = basketRepository.findByCustomerId(customerId);
        return basket.map(value -> new BasketResponse(
                        value.getCustomerId(),
                        value.getItems()
                ))
                .orElse(null);
    }

    public BasketResponse addItemToBasket(
            String customerId,
            BasketAddItemRequest basketAddItemRequest
    ) {
        Basket basket = basketRepository.findById(customerId)
                .orElse(new Basket(customerId, new ArrayList<>()));

        // transform new items
        List<BasketItem> newBasketItems = basketAddItemRequest.items().stream()
                .map(basketItemRequest -> {
                    ProductResponse productResponse = productClient
                            .getProductById(basketItemRequest.productId());
                    return new BasketItem(
                            productResponse.productId(),
                            productResponse.productName(),
                            productResponse.basePrice(),
                            productResponse.manufacturer(),
                            productResponse.categoryId(),
                            ItemType.PHYSICAL,
                            basketItemRequest.quantity()
                    );
                }).collect(Collectors.toList());

        // add/update items
        List<BasketItem> existingItems = basket.getItems() != null ? basket.getItems() : new ArrayList<>();
        for(BasketItem newBasketItem : newBasketItems){
            boolean itemExists = false;
            for(BasketItem existingItem : existingItems){
                if(existingItem.getProductId().equals(newBasketItem.getProductId())){
                    existingItem.setQuantity(existingItem.getQuantity() + newBasketItem.getQuantity());
                    itemExists = true;
                    break;
                }
            }
            if(!itemExists){
                existingItems.add(newBasketItem);
            }
        }

        basket.setItems(existingItems);
        Basket updatedBasket = basketRepository.save(basket);

        return new BasketResponse(updatedBasket.getCustomerId(), updatedBasket.getItems());
    }


    public BasketTotalPriceResponse calculateTotalBasketPrice(
            String customerId
    ) {
        Basket basket = basketRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Basket not found for customer id: " + customerId));

        BigDecimal totalPrice = basket.getItems().stream()
                .map(item -> item.getBasePrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new BasketTotalPriceResponse(customerId, totalPrice);
    }
}