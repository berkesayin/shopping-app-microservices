package dev.berke.app.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${application.config.product-url}")
public interface ProductClient {

    @GetMapping("/{product-id}/available-quantity")
    double getAvailableQuantityByProductId(@PathVariable("product-id") Integer productId);

}