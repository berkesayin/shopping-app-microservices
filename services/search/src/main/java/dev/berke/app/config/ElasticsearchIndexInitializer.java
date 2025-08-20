package dev.berke.app.config;

import dev.berke.app.order.document.OrderDocument;
import dev.berke.app.product.document.ProductDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchIndexInitializer {

    private final ElasticsearchOperations elasticsearchOperations;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createIndexAndMapping(ProductDocument.class);
        createIndexAndMapping(OrderDocument.class);
        log.info("Elasticsearch index initialization finished!");
    }

    private void createIndexAndMapping(Class<?> documentClass) {
        IndexOperations indexOps = elasticsearchOperations.indexOps(documentClass);

        if (indexOps.exists()) {
            log.info("Index for {} already exists.", documentClass.getSimpleName());
        } else {
            log.info("Index for {} does not exist.", documentClass.getSimpleName());
            indexOps.create();
            indexOps.putMapping();
            log.info("Created index and mapping for {}.", documentClass.getSimpleName());
        }
    }
}