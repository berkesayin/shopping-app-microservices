package dev.berke.app.customer;

import dev.berke.app.constants.CustomerConstants;
import dev.berke.app.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public String createCustomer(CustomerRequest customerRequest) {
        var customer = customerRepository.save(customerMapper.toCustomer(customerRequest));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest customerRequest) {
        var customer = customerRepository.findById(customerRequest.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format(CustomerConstants.CUSTOMER_NOT_FOUND_MESSAGE, customerRequest.id())
                ));
        mergeCustomer(customer, customerRequest);
        customerRepository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerRequest customerRequest) {
        if (StringUtils.isNotBlank(customerRequest.name())) {
            customer.setName(customerRequest.name());
        }
        if (StringUtils.isNotBlank(customerRequest.surname())) {
            customer.setSurname(customerRequest.surname());
        }
        if (StringUtils.isNotBlank(customerRequest.gsmNumber())) {
            customer.setGsmNumber(customerRequest.gsmNumber());
        }
        if (StringUtils.isNotBlank(customerRequest.email())) {
            customer.setEmail(customerRequest.email());
        }
        if (StringUtils.isNotBlank(customerRequest.identityNumber())) {
            customer.setIdentityNumber(customerRequest.identityNumber());
        }
        if (StringUtils.isNotBlank(customerRequest.registrationAddress())) {
            customer.setRegistrationAddress(customerRequest.registrationAddress());
        }
        if (StringUtils.isNotBlank(customerRequest.city())) {
            customer.setCity(customerRequest.city());
        }
        if (StringUtils.isNotBlank(customerRequest.country())) {
            customer.setCountry(customerRequest.country());
        }
        if (StringUtils.isNotBlank(customerRequest.zipCode())) {
            customer.setZipCode(customerRequest.zipCode());
        }
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public Boolean checkCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .isPresent();
    }

    public CustomerResponse findCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format(CustomerConstants.CUSTOMER_NOT_FOUND_BY_ID, customerId)));
    }

    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }
}