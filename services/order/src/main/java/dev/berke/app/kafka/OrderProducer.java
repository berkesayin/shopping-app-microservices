package dev.berke.app.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;

    public void sendOrderConfirmation (OrderConfirmation orderConfirmation) {
        log.info("Sending order confirmation");

        // Create a Kafka message using MessageBuilder
        Message<OrderConfirmation> message = MessageBuilder
                .withPayload(orderConfirmation) // Set the payload to be the orderConfirmation
                .setHeader(KafkaHeaders.TOPIC, "order-topic") // Set the Kafka topic header to "order-topic"
                .build(); // Build the Kafka message

        // Send the message to Kafka using the KafkaTemplate
        kafkaTemplate.send(message);
    }
}