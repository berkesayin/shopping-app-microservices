package dev.berke.app.payment;

import dev.berke.app.card.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<CreditCard> findByCustomerId(String customerId);
}
