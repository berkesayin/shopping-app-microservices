package dev.berke.app.customer;

import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public Customer toCustomer(CustomerRequest customerRequest) {
        if (customerRequest == null) {
            return null;
        }

        return Customer.builder()
                .id(customerRequest.id())
                .name(customerRequest.name())
                .surname(customerRequest.surname())
                .gsmNumber(customerRequest.gsmNumber())
                .email(customerRequest.email())
                .identityNumber(customerRequest.identityNumber())
                .registrationAddress(customerRequest.registrationAddress())
                .city(customerRequest.city())
                .country(customerRequest.country())
                .zipCode(customerRequest.zipCode())
                .build();
    }

    public CustomerResponse fromCustomer(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getSurname(),
                customer.getGsmNumber(),
                customer.getEmail(),
                customer.getIdentityNumber(),
                customer.getRegistrationAddress(),
                customer.getCity(),
                customer.getCountry(),
                customer.getZipCode(),
                customer.getAddress()
        );
    }
}
