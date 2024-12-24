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
public class PaymentNotificationProducer {

    // KafkaTemplate for sending messages to Kafka broker
    private final KafkaTemplate<String, PaymentNotificationRequest> kafkaTemplate;

    // Send a payment notification to the Kafka topic
    public void sendPaymentNotification(PaymentNotificationRequest paymentNotificationRequest) {
        log.info("Sending notification with body <{}>", paymentNotificationRequest);

        // Create a message to be sent to Kafka
        Message<PaymentNotificationRequest> message = MessageBuilder
                // Sets the payload of the message to the provided request
                .withPayload(paymentNotificationRequest)
                .setHeader(KafkaHeaders.TOPIC, "payment-topic")
                .build();

        // Send the message to the Kafka payment-topic
        kafkaTemplate.send(message);
    }
}