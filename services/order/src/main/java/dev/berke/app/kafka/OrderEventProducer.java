package dev.berke.app.kafka;

import dev.berke.app.events.OrderCreatedEvent;
import dev.berke.app.events.OrderReceivedEvent;
import dev.berke.app.events.OrderStatusUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderReceivedEvent> kafkaTemplate;

    @Value("${app.kafka.topics.order-received}")
    private String orderReceivedTopic;

    @Value("${app.kafka.topics.order-created}")
    private String orderCreatedTopic;

    @Value("${app.kafka.topics.order-status-updated}")
    private String orderStatusUpdatedTopic;

    public void sendOrderConfirmation(OrderReceivedEvent event) {
        log.info("Sending order received event to topic: {} for order reference: {}",
                orderReceivedTopic, event.reference());

        sendMessage(orderReceivedTopic, event);
    }

    public void sendOrderCreated(OrderCreatedEvent event) {
        log.info("Sending order created event to topic: {} for order reference: {}",
                orderCreatedTopic, event.reference());

        sendMessage(orderCreatedTopic, event);
    }

    public void sendOrderStatusUpdate(OrderStatusUpdatedEvent event) {
        log.info("Sending order status updated event to topic: {} for order ID: {}. New status: {}",
                orderStatusUpdatedTopic, event.orderId(), event.newStatus());

        sendMessage(orderStatusUpdatedTopic, event);
    }

    private <T> void sendMessage(String topic, T payload) {
        try {
            Message<T> message = MessageBuilder
                    .withPayload(payload)
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .build();

            kafkaTemplate.send(message);
            log.debug("Sent message to topic: {}", topic);
        } catch (Exception e) {
            log.error("Failed to send message to Kafka topic: {}", topic, e);
        }
    }
}