package dev.berke.app.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "customer-service", url = "${application.config.customer-url}")
public interface CustomerClient {

    @GetMapping("/{customer-id}")
    Map<String, Object> getCustomerById(@PathVariable("customer-id") String customerId);

    @GetMapping("/{customerId}/billing-addresses/active")
    AddressResponse getActiveBillingAddress(@PathVariable("customerId") String customerId);

    @GetMapping("/{customerId}/shipping-addresses/active")
    AddressResponse getActiveShippingAddress(@PathVariable("customerId") String customerId);
}
