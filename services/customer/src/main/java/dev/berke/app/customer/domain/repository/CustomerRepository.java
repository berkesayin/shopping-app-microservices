package dev.berke.app.customer.domain.repository;

import dev.berke.app.customer.domain.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    boolean existsByEmail(String email);
}
