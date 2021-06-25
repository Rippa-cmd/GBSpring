import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import persist.Product;
import persist.ProductCart;
import persist.ProductRepository;

import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class AppConfig {

    private final AtomicLong id = new AtomicLong(0);

    @Bean
    @Scope("prototype")
    public Product product() {
        long curId = id.getAndIncrement();
        return new Product(curId, "Product " + curId, (int) (Math.random() * (100 * (curId+1))));
    }

    @Bean
    public ProductRepository repo() {
        ProductRepository productRepository = new ProductRepository();
        for (int i = 0; i < 5; i++) {
            productRepository.save(product());
        }
        return productRepository;
    }

    @Bean
    @Scope("prototype")
    public ProductCart cart() {
        return new ProductCart();
    }
}
