package dev.berke.app.kafka;

import dev.berke.app.email.OrderConfirmationEmail;
import dev.berke.app.events.OrderReceivedEvent;
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
public class OrderEventListener {

    private final NotificationRepository notificationRepository;
    private final OrderConfirmationEmail orderConfirmationEmail;

    // Kafka listener to consume order confirmation messages from the order-topic
    @KafkaListener(topics = "order-topic")
    public void consumeOrderConfirmationNotification(
            OrderReceivedEvent orderReceivedEvent
    ) throws MessagingException {
        log.info(String.format("Consuming the message from order-topic,  Topic:: %s", orderReceivedEvent));
        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderReceivedEvent(orderReceivedEvent)
                        .build()
        );

        orderConfirmationEmail.sendOrderConfirmationEmail(
                orderReceivedEvent.customerName(),
                orderReceivedEvent.customerEmail(),
                orderReceivedEvent.reference(),
                orderReceivedEvent.paymentMethod(),
                orderReceivedEvent.productsToPurchase(),
                orderReceivedEvent.totalPrice()
        );
    }
}

