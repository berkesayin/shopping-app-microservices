package dev.berke.app.customer;

import dev.berke.app.address.AddressMapper;
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
    private final AddressMapper addressMapper;

    public String createCustomer(CustomerCreateRequest customerCreateRequest) {
        if(customerRepository.existsByEmail(customerCreateRequest.email())) {
            throw new IllegalArgumentException("Email already in use");
        }
        var customer = customerRepository.save(customerMapper.toCustomer(customerCreateRequest));
        return customer.getId();
    }

    public CustomerResponse updateCustomer(CustomerUpdateRequest customerUpdateRequest) {
        var customer = customerRepository.findById(customerUpdateRequest.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format(CustomerConstants
                                .CUSTOMER_NOT_FOUND_MESSAGE, customerUpdateRequest.id())
                ));
        if (StringUtils.isNotBlank(customerUpdateRequest.name())) {
            customer.setName(customerUpdateRequest.name());
        }
        if (StringUtils.isNotBlank(customerUpdateRequest.surname())) {
            customer.setSurname(customerUpdateRequest.surname());
        }
        if (StringUtils.isNotBlank(customerUpdateRequest.gsmNumber())) {
            customer.setGsmNumber(customerUpdateRequest.gsmNumber());
        }
        if (StringUtils.isNotBlank(customerUpdateRequest.email())) {
            customer.setEmail(customerUpdateRequest.email());
        }
        if (StringUtils.isNotBlank(customerUpdateRequest.password())) {
            customer.setPassword(customerUpdateRequest.password());
        }
        if (StringUtils.isNotBlank(customerUpdateRequest.identityNumber())) {
            customer.setIdentityNumber(customerUpdateRequest.identityNumber());
        }
        if (StringUtils.isNotBlank(customerUpdateRequest.registrationAddress())) {
            customer.setRegistrationAddress(customerUpdateRequest.registrationAddress());
        }
        customerRepository.save(customer);
        return customerMapper.fromCustomer(customer);
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