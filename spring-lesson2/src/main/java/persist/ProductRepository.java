package persist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ProductRepository {

    private final List<Product> productList = new ArrayList<>();
    private AtomicLong identity = new AtomicLong(0);

    public List<Product> findAll() {
        return productList;
    }

    public Product findById(long id) {
        for (Product product : productList) {
            if (product.getId() == id)
                return product;
        }
        return null;
    }

    public void save(Product product) {
//        if (product.getId() == null) {
//            long id = identity.incrementAndGet();
//            product.setId(id);
//        }
        productList.add(product);
    }
}
