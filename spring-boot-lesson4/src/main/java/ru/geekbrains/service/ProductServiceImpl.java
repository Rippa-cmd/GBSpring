package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.geekbrains.persist.Product;
import ru.geekbrains.persist.ProductRepository;
import ru.geekbrains.persist.ProductSpecifications;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    public ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> findWithFilters(ProductSearchFilters productSearchFilters) {
        Specification<Product> spec = Specification.where(null);
        Sort sortConfiguration;
        if (productSearchFilters.getProductNameFilter() != null && !productSearchFilters.getProductNameFilter().isBlank())
            spec = spec.and(ProductSpecifications.productNamePrefix(productSearchFilters.getProductNameFilter()));

        if (productSearchFilters.getMinCostFilter() != null)
            spec = spec.and(ProductSpecifications.minCost(productSearchFilters.getMinCostFilter()));

        if (productSearchFilters.getMaxCostFilter() != null)
            spec = spec.and(ProductSpecifications.maxCost(productSearchFilters.getMaxCostFilter()));

        if (productSearchFilters.getSize() == null || productSearchFilters.getSize() <= 0)
            productSearchFilters.setSize(3);

        if (productSearchFilters.getPage() == null || productSearchFilters.getPage() <= 0)
            productSearchFilters.setPage(1);

        if (productSearchFilters.getSortField() != null && !productSearchFilters.getSortField().isBlank())
            sortConfiguration = Sort.by(productSearchFilters.getSortField());
        else
            sortConfiguration = Sort.by("id");

        if ("desc".equals(productSearchFilters.getSortDirection()))
            sortConfiguration = sortConfiguration.descending();

        return productRepository.findAll(spec, PageRequest.of(productSearchFilters.getPage() - 1,
                productSearchFilters.getSize(), sortConfiguration));
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
