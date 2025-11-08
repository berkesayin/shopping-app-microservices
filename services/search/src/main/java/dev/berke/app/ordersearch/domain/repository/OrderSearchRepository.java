package dev.berke.app.ordersearch.domain.repository;

import dev.berke.app.ordersearch.domain.document.OrderDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSearchRepository extends ElasticsearchRepository<OrderDocument, String> {
}
