package dev.berke.app.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import dev.berke.app.events.OrderCreatedEvent;
import dev.berke.app.order.document.OrderDocument;
import dev.berke.app.order.mapper.OrderMapper;
import dev.berke.app.order.repository.OrderSearchRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderSearchService {

    private final OrderSearchRepository repository;
    private final OrderMapper mapper;

    public void indexOrder(OrderCreatedEvent event) {
        try {
            log.info("Indexing order with reference: {}", event.reference());

            OrderDocument document = mapper.toDocument(event);
            repository.save(document);

            log.info("Indexed order document with ID: {}", document.getOrderId());
        } catch (Exception e) {
            log.error("Failed to index order document for reference: {}", event.reference(), e);

            throw new RuntimeException("Error indexing order: ", e);
        }
    }
}