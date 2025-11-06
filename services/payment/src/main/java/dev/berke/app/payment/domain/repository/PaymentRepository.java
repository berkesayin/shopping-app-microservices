package dev.berke.app.payment.domain.repository;

import dev.berke.app.payment.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByCustomerId(String customerId);
}
