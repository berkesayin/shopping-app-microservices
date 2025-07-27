package dev.berke.app.kafka;

import dev.berke.app.email.PaymentConfirmationEmail;
import dev.berke.app.events.PaymentReceivedEvent;
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
public class PaymentEventListener {

    private final NotificationRepository notificationRepository;
    private final PaymentConfirmationEmail paymentConfirmationEmail;

    // Kafka listener to consume payment confirmation messages from the payment-topic
    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(
            PaymentReceivedEvent paymentReceivedEvent
    ) throws MessagingException {
        log.info(String.format("Consuming the message from payment-topic,  Topic:: %s", paymentReceivedEvent));
        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentReceivedEvent(paymentReceivedEvent)
                        .build()
        );


        paymentConfirmationEmail.sendPaymentSuccessEmail(
                paymentReceivedEvent.customerName(),
                paymentReceivedEvent.email(),
                paymentReceivedEvent.totalPrice(),
                paymentReceivedEvent.paymentMethod()
        );
    }
}

