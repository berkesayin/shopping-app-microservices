package dev.berke.app.notification.domain.model;

import dev.berke.app.consumer.event.OrderReceivedEvent;
import dev.berke.app.consumer.event.PaymentReceivedEvent;
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
    private OrderReceivedEvent orderReceivedEvent;
    private PaymentReceivedEvent paymentReceivedEvent;
}

