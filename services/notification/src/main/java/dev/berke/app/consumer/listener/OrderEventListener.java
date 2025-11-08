package dev.berke.app.consumer.listener;

import dev.berke.app.notification.infrastructure.email.OrderConfirmationEmail;
import dev.berke.app.consumer.event.OrderReceivedEvent;
import dev.berke.app.notification.domain.model.Notification;
import dev.berke.app.notification.domain.repository.NotificationRepository;
import dev.berke.app.notification.domain.model.NotificationType;
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
    
    @KafkaListener(topics = "order-confirmations")
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

