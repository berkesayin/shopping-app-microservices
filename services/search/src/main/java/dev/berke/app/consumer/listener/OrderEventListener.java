package dev.berke.app.consumer.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.berke.app.consumer.event.OrderCreatedEvent;
import dev.berke.app.ordersearch.application.OrderSearchService;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = "${app.kafka.topics.order-created}", groupId = "search-service-group")
public class OrderEventListener {

    private final OrderSearchService orderSearchService;

    @KafkaHandler
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Kafka listener received an OrderCreatedEvent for reference: {}", event.reference());
        orderSearchService.indexOrder(event);
    }

}