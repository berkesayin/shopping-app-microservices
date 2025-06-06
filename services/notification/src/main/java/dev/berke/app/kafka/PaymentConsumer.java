package dev.berke.app.kafka;

import dev.berke.app.email.PaymentConfirmationEmail;
import dev.berke.app.kafka.payment.PaymentConfirmRequest;
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
public class PaymentConsumer {

    private final NotificationRepository notificationRepository;
    private final PaymentConfirmationEmail paymentConfirmationEmail;

    // Kafka listener to consume payment confirmation messages from the payment-topic
    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(
            PaymentConfirmRequest paymentConfirmRequest
    ) throws MessagingException {
        log.info(String.format("Consuming the message from payment-topic,  Topic:: %s", paymentConfirmRequest));
        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmRequest(paymentConfirmRequest)
                        .build()
        );


        paymentConfirmationEmail.sendPaymentSuccessEmail(
                paymentConfirmRequest.customerName(),
                paymentConfirmRequest.email(),
                paymentConfirmRequest.totalPrice(),
                paymentConfirmRequest.paymentMethod()
        );
    }
}

