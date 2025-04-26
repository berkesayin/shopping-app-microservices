package dev.berke.app.customer;

import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public Customer toCustomer(CustomerCreateRequest customerCreateRequest) {
        if (customerCreateRequest == null) {
            return null;
        }

        return Customer.builder()
                .name(customerCreateRequest.name())
                .surname(customerCreateRequest.surname())
                .gsmNumber(customerCreateRequest.gsmNumber())
                .email(customerCreateRequest.email())
                .password(customerCreateRequest.password())
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