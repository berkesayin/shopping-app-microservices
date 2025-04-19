package dev.berke.app.kafka;

import dev.berke.app.email.OrderConfirmationEmail;
import dev.berke.app.email.PaymentSuccessEmail;
import dev.berke.app.kafka.order.OrderConfirmationRequest;
import dev.berke.app.kafka.payment.PaymentConfirmationRequest;
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
            OrderConfirmationRequest orderConfirmationRequest
    ) throws MessagingException {
        log.info(String.format("Consuming the message from order-topic,  Topic:: %s", orderConfirmationRequest));
        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmationRequest(orderConfirmationRequest)
                        .build()
        );

        // send email ==>  for consumeOrderConfirmationNotification() method
        var customerEmail = "berkesayin@gmail.com";
        orderConfirmationEmail.sendOrderConfirmationEmail(
                orderConfirmationRequest.customerEmail(),
                orderConfirmationRequest.customerId(),
                orderConfirmationRequest.reference(),
                orderConfirmationRequest.paymentMethod()
        );
    }

    // Kafka listener to consume payment success messages from the payment-topic
    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(
            PaymentConfirmationRequest paymentConfirmationRequest
    ) throws MessagingException {
        log.info(String.format("Consuming the message from payment-topic,  Topic:: %s", paymentConfirmationRequest));
        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmationRequest(paymentConfirmationRequest)
                        .build()
        );

        // send email ==>  for sendPaymentSuccessEmail() method
        var customerName = paymentConfirmationRequest.name() + " "
                + paymentConfirmationRequest.surname();

        paymentSuccessEmail.sendPaymentSuccessEmail(
                paymentConfirmationRequest.email(),
                customerName,
                paymentConfirmationRequest.totalBasketPrice(),
                paymentConfirmationRequest.basketItems()
        );
    }
}

