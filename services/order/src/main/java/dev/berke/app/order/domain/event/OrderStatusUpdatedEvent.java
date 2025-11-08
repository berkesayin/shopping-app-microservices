package dev.berke.app.order.domain.event;

import java.time.LocalDateTime;

public record OrderStatusUpdatedEvent(
        String orderId,
        String newStatus,
        LocalDateTime updatedDate
) {
}