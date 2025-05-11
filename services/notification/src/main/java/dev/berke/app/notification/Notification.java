package dev.berke.app.notification;

import dev.berke.app.kafka.order.OrderConfirmRequest;
import dev.berke.app.kafka.payment.PaymentConfirmRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Notification {

    @Id
    private String id;
    private NotificationType type;
    private LocalDateTime notificationDate;
    private OrderConfirmRequest orderConfirmRequest;
    private PaymentConfirmRequest paymentConfirmRequest;
}

