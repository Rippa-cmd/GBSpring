package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.geekbrains.persist.Product;
import ru.geekbrains.persist.ProductRepository;
import ru.geekbrains.persist.User;
import ru.geekbrains.persist.UserRepository;

@Controller
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final ProductRepository productRepository;

    @Autowired
    private ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String listPage(Model model) {
        logger.info("Product list page requested");

        model.addAttribute("products", productRepository.findAll());
        return "products";
    }

    @GetMapping("/new")
    public String newProductForm(Model model) {
        logger.info("New product page requested");

        model.addAttribute("product", new Product());
        return "product_form";
    }

    @GetMapping("/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productRepository.findById(id));
        return "edition_product_form";
    }

    @PostMapping
    public String update(Product product) {
        logger.info("Saving product");

        if (product.getId() == null)
            productRepository.insert(product);
        else
            productRepository.update(product);
        return "redirect:/product";
    }

    @GetMapping("/delete_{id}")
    public String deleteProduct(@PathVariable("id") Long id, Model model) {
        logger.info("Deleting product");
        //productRepository.delete(id);
        model.addAttribute("product", productRepository.findById(id));

        return "delete_form";
    }

    @GetMapping("/confirmed_deletion")
    public String confirmedDelete(Long id) {
        logger.info("Confirmed deleting product - id" + id);

        productRepository.delete(id);

        return "redirect:/product";
    }

}
