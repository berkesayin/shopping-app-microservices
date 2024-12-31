package dev.berke.app.customer;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "customer-service",
        url = "${application.config.customer-url}"
)
public interface CustomerClient {

    @GetMapping("/{customer-id}")
    CustomerResponse getCustomerById(@PathVariable("customer-id") String customerId);

    /*
    @PostMapping(
            value = "/billing-address",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    String createBillingAddress(@RequestBody AddressRequest addressRequest);

    @PostMapping(
            value = "/shipping-address",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    String createShippingAddress(@RequestBody AddressRequest addressRequest);
    */
}