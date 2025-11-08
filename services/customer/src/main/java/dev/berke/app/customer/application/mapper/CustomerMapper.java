package dev.berke.app.customer.application.mapper;

import dev.berke.app.customer.domain.model.Customer;
import dev.berke.app.customer.api.dto.CustomerDataRequest;
import dev.berke.app.customer.api.dto.CustomerResponse;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public Customer toCustomer(CustomerDataRequest customerDataRequest) {
        if (customerDataRequest == null) {
            return null;
        }

        return Customer.builder()
                .name(customerDataRequest.name())
                .surname(customerDataRequest.surname())
                .gsmNumber(customerDataRequest.gsmNumber())
                .email(customerDataRequest.email())
                .build();
    }

    public CustomerResponse fromCustomer(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getSurname(),
                customer.getGsmNumber(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getIdentityNumber(),
                customer.getRegistrationAddress(),
                customer.getBillingAddresses(),
                customer.getShippingAddresses(),
                customer.getActiveBillingAddressId(),
                customer.getActiveShippingAddressId()
        );
    }
}