package dev.berke.app.kafka;

import dev.berke.app.events.ProductPublishedEvent;
import dev.berke.app.events.ProductUnpublishedEvent;
import dev.berke.app.product.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = "${app.kafka.topics.product-events}", groupId = "search-service-group")
public class ProductEventListener {

    private final ProductSearchService productSearchService;

    @KafkaHandler
    public void handleProductPublished(ProductPublishedEvent event) {
        log.info("Kafka listener received a ProductPublishedEvent");
        productSearchService.indexProduct(event);
    }

    @KafkaHandler
    public void handleProductUnpublished(ProductUnpublishedEvent event) {
        log.info("Kafka listener received a ProductUnpublishedEvent");
        productSearchService.deleteProduct(event.productId());
    }

    @KafkaHandler(isDefault = true)
    public void handleUnknown(Object object) {
        log.warn("Received an unknown event type from Kafka: {}", object);
    }
}