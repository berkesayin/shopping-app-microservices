package dev.berke.app.productsearch.domain.repository;

import dev.berke.app.productsearch.domain.document.ProductDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Integer>,
        CustomProductSearchRepository {

    @Query("{\"match\": {\"product_name.autocomplete\": \"?0\"}}")
    List<ProductDocument> findByProductNameAutocomplete(String query);
}