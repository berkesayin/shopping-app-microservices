package dev.berke.app.notification.domain.repository;

import dev.berke.app.notification.domain.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}