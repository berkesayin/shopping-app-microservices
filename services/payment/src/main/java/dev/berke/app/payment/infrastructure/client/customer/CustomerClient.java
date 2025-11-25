package dev.berke.app.payment.infrastructure.client.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Optional;

@FeignClient(name = "customer-service", path = "/api/v1/customers")
public interface CustomerClient {

    @GetMapping("/me")
    Optional<CustomerResponse> getProfile();

    @GetMapping("/me/billing-addresses/active")
    AddressResponse getActiveBillingAddress();

    @GetMapping("/me/shipping-addresses/active")
    AddressResponse getActiveShippingAddress();
}
