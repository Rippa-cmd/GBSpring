package persist;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ProductCart {
    private final List<Product> cart = new ArrayList<>();

    @Autowired
    private ProductRepository productRepository;

    public void addProduct(long productId) {
        Product selectedProduct = productRepository.findById(productId);
        if (selectedProduct == null)
            return;
        cart.add(selectedProduct);
    }

    public void deleteProduct(long productId) {
        Product selectedProduct = productRepository.findById(productId);
        if (selectedProduct == null)
            return;
        cart.remove(selectedProduct);
//        for (Product product : cart) {
//            if (product.equals(selectedProduct))
//                cart.remove()
//        }
    }
}
