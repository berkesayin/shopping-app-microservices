package dev.berke.app.product;

import dev.berke.app.constants.ProductConstants;
import dev.berke.app.kafka.ProductEventProducer;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductEventProducer productEventProducer;

    public ProductResponse createProduct(ProductRequest productRequest) {
        var product = productMapper.toProduct(productRequest);
        var savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }

    @Transactional
    public ProductResponse setProductStatus(Integer productId, Integer newStatus) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ProductConstants.PRODUCT_NOT_FOUND_MESSAGE + productId
                ));

        if (product.getStatus().equals(newStatus)) {
            throw new InvalidRequestException("Product status is already " + newStatus);
        }

        Integer oldStatus = product.getStatus();
        product.setStatus(newStatus);
        Product updatedProduct = productRepository.save(product);

        if (oldStatus == 0 && newStatus == 1) {
            productEventProducer.sendProductPublishedEvent(updatedProduct);
        } else if (oldStatus == 1 && newStatus == 0) {
            productEventProducer.sendProductUnpublishedEvent(updatedProduct.getProductId());
        }

        return productMapper.toProductResponse(updatedProduct);
    }

    public ProductResponse getProductById(Integer productId) {
        return productRepository.findById(productId)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        ProductConstants.PRODUCT_NOT_FOUND_MESSAGE + productId
                ));
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    public Integer getCategoryIdOfProduct(Integer productId) {
        return productRepository.findById(productId)
                .map(product -> product.getCategory().getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ProductConstants.PRODUCT_NOT_FOUND_MESSAGE + productId
                ));
    }
}
