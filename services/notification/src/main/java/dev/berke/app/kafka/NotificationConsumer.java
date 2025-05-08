package dev.berke.app.kafka;

import dev.berke.app.email.OrderConfirmationEmail;
import dev.berke.app.email.PaymentSuccessEmail;
import dev.berke.app.kafka.order.OrderConfirmation;
import dev.berke.app.kafka.payment.PaymentConfirmation;
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
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final OrderConfirmationEmail orderConfirmationEmail;
    private final PaymentSuccessEmail paymentSuccessEmail;

    // Kafka listener to consume order confirmation messages from the order-topic
    @KafkaListener(topics = "order-topic")
    public void consumeOrderConfirmationNotification(
            OrderConfirmation orderConfirmation
    ) throws MessagingException {
        log.info(String.format("Consuming the message from order-topic,  Topic:: %s", orderConfirmation));
        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmation(orderConfirmation)
                        .build()
        );

        // send email ==>  for consumeOrderConfirmationNotification() method
        var customerName = orderConfirmation.customer().firstname() + " "
                + orderConfirmation.customer().lastname();

        orderConfirmationEmail.sendOrderConfirmationEmail(
                orderConfirmation.customer().email(),
                customerName,
                orderConfirmation.totalAmount(),
                orderConfirmation.reference(),
                orderConfirmation.products()
        );
    }

    // Kafka listener to consume payment success messages from the payment-topic
    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(
            PaymentConfirmation paymentConfirmation
    ) throws MessagingException {
        log.info(String.format("Consuming the message from payment-topic,  Topic:: %s", paymentConfirmation));
        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmation(paymentConfirmation)
                        .build()
        );

        // send email ==>  for sendPaymentSuccessEmail() method
        var customerName = paymentConfirmation.customerFirstname() + " "
                + paymentConfirmation.customerLastname();

        paymentSuccessEmail.sendPaymentSuccessEmail(
                paymentConfirmation.customerEmail(),
                customerName,
                paymentConfirmation.amount(),
                paymentConfirmation.reference()
        );
    }
}

