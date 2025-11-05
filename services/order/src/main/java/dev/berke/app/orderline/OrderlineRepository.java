package dev.berke.app.orderline;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderlineRepository extends JpaRepository<Orderline, Integer> {
    List<Orderline> findAllByOrderId(Integer orderId);
}
