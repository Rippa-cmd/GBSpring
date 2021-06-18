package ru.geekbrains;

import ru.geekbrains.persist.Product;
import ru.geekbrains.persist.ProductRepository;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class BootstrapListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();

        ProductRepository productRepository = new ProductRepository();
        productRepository.save(new Product(1L, "Product 1", 8));
        productRepository.save(new Product(2L, "Product 2", 16));
        productRepository.save(new Product(3L, "Product 3", 32));
        productRepository.save(new Product(4L, "Product 4", 64));
        productRepository.save(new Product(5L, "Product 5", 128));
        productRepository.save(new Product(6L, "Product 6", 256));
        productRepository.save(new Product(7L, "Product 7", 512));
        productRepository.save(new Product(8L, "Product 8", 1024));
        productRepository.save(new Product(9L, "Product 9", 2048));
        productRepository.save(new Product(10L, "Product 10", 4096));

        sc.setAttribute("product repository", productRepository);
    }
}
