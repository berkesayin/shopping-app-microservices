package dev.berke.app.product;

import dev.berke.app.events.ProductPublishedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;

    public void indexProduct(ProductPublishedEvent event) {
        log.info("INDEX event received for product ID: {}", event.productId());

        ProductDocument document = ProductDocument.builder()
                .productId(event.productId())
                .productName(event.productName())
                .categoryId(event.categoryId())
                .categoryName(event.categoryName())
                .basePrice(event.basePrice())
                .minPrice(event.minPrice())
                .manufacturer(event.manufacturer())
                .sku(event.sku())
                .status(event.status())
                .createdOn(event.createdOn())
                .build();

        log.info("Document to be indexed: {}", document);

        productSearchRepository.save(document);
        log.info("Successfully indexed product with ID: {}", document.getProductId());
    }

    public void deleteProduct(Integer productId) {
        log.info("DELETE event received for product ID: {}", productId);
        productSearchRepository.deleteById(productId);
        log.info("Successfully deleted product with ID: {}", productId);
    }
}