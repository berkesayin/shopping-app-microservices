package dev.berke.app.kafka;

import dev.berke.app.events.ProductPublishedEvent;
import dev.berke.app.events.ProductUnpublishedEvent;
import dev.berke.app.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductEventProducer {

    private static final Logger log = LoggerFactory.getLogger(ProductEventProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String productEventsTopic;

    public ProductEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.topics.product-events}") String productEventsTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.productEventsTopic = productEventsTopic;
    }

    public void sendProductPublishedEvent(Product product) {
        log.info("Preparing to send ProductPublishedEvent for product ID: {}", product.getProductId());
        try {
            ProductPublishedEvent event = new ProductPublishedEvent(
                    product.getProductId(),
                    product.getProductName(),
                    product.getCategory().getCategoryId(),
                    product.getCategory().getCategoryName(),
                    product.getBasePrice(),
                    product.getMinPrice(),
                    product.getManufacturer(),
                    product.getSku(),
                    1,
                    product.getCreatedOn()
            );
            kafkaTemplate.send(productEventsTopic, String.valueOf(event.productId()), event);
            log.info("Sent ProductPublishedEvent for product ID: {}", product.getProductId());
        } catch (Exception e) {
            log.error("Failed to send ProductPublishedEvent for product ID: {}", product.getProductId(), e);
        }
    }

    public void sendProductUnpublishedEvent(Integer productId) {
        log.info("Preparing to send ProductUnpublishedEvent for product ID: {}", productId);
        try {
            ProductUnpublishedEvent event = new ProductUnpublishedEvent(productId);
            kafkaTemplate.send(productEventsTopic, String.valueOf(event.productId()), event);
            log.info("Sent ProductUnpublishedEvent for product ID: {}", productId);
        } catch (Exception e) {
            log.error("Failed to send ProductUnpublishedEvent for product ID: {}", productId, e);
        }
    }
}