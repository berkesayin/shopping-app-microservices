package dev.berke.app.kafka;

import dev.berke.app.email.OrderConfirmationEmail;
import dev.berke.app.kafka.order.OrderConfirmRequest;
import dev.berke.app.notification.Notification;
import dev.berke.app.notification.NotificationRepository;
import dev.berke.app.notification.NotificationType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final NotificationRepository notificationRepository;
    private final OrderConfirmationEmail orderConfirmationEmail;

    // Kafka listener to consume order confirmation messages from the order-topic
    @KafkaListener(topics = "order-topic")
    public void consumeOrderConfirmationNotification(
            OrderConfirmRequest orderConfirmRequest
    ) throws MessagingException {
        log.info(String.format("Consuming the message from order-topic,  Topic:: %s", orderConfirmRequest));
        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmRequest(orderConfirmRequest)
                        .build()
        );

        orderConfirmationEmail.sendOrderConfirmationEmail(
                orderConfirmRequest.customerName(),
                orderConfirmRequest.customerEmail(),
                orderConfirmRequest.reference(),
                orderConfirmRequest.paymentMethod(),
                orderConfirmRequest.productsToPurchase(),
                orderConfirmRequest.totalPrice()
        );
    }
}

