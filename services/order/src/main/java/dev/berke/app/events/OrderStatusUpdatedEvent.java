package dev.berke.app.events;

import java.time.LocalDateTime;

public record OrderStatusUpdatedEvent(
        String orderId,
        String newStatus,
        LocalDateTime updatedDate
) {
}