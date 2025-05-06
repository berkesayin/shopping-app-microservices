package dev.berke.app.basket;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketRepository extends CrudRepository<Basket, String> {

    Optional<Basket> findByCustomerId(String customerId);
}