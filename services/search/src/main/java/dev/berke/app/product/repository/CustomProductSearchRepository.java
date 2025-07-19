package dev.berke.app.product.repository;

import dev.berke.app.product.dto.ProductSearchRequest;
import dev.berke.app.product.document.ProductDocument;
import org.springframework.data.elasticsearch.core.SearchHits;

public interface CustomProductSearchRepository {

    SearchHits<ProductDocument> search(ProductSearchRequest request);
}
