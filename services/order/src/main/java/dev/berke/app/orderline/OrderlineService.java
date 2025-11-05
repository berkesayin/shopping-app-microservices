package dev.berke.app.orderline;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderlineService {

    private final OrderlineRepository orderlineRepository;
    private final OrderlineMapper orderlineMapper;

    public Integer saveOrderLine(OrderlineRequest orderlineRequest) {
        var order = orderlineMapper.toOrderLine(orderlineRequest);
        return orderlineRepository.save(order).getId();
    }

    public List<OrderlineResponse> getOrderLinesByOrderId(Integer orderId) {
        return orderlineRepository.findAllByOrderId(orderId)
                .stream()
                .map(orderlineMapper::toOrderLineResponse)
                .collect(Collectors.toList());
    }
}