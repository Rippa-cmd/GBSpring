package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.persist.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Page<Product> findWithFilters(ProductSearchFilters productSearchFilters);

    Optional<Product> findById(Long id);

    void save(Product product);

    void deleteById(Long id);

    List<Product> findAll();
}
