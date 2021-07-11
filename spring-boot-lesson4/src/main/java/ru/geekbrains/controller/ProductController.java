package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import ru.geekbrains.persist.Product;
import ru.geekbrains.persist.ProductRepository;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductRepository productRepository;

    @Autowired
    private ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String listPage(Model model,
                           @RequestParam("productNameFilter") Optional<String> productNameFilter,
                           @RequestParam("minCostFilter") Optional<BigDecimal> minCostFilter,
                           @RequestParam("maxCostFilter") Optional<BigDecimal> maxCostFilter) {
        logger.info("Product list page requested");

        List<Product> products = productNameFilter.map(productRepository::findProductsByNameStartsWith).orElse(productRepository.findAll());

        if (minCostFilter.isPresent())
            products = products.stream().filter(product -> product.getCost().compareTo(minCostFilter.get()) >= 0).collect(Collectors.toList());

        if (maxCostFilter.isPresent())
            products = products.stream().filter(product -> product.getCost().compareTo(maxCostFilter.get()) <= 0).collect(Collectors.toList());
//        List<Product> productsCost = minCostFilter.map( min -> maxCostFilter
//                .map(max -> productRepository.findProductsByCostBetween(min, max))
//                .orElse(productRepository.findProductsByCostGreaterThan(min)))
//                .orElse(maxCostFilter.map(productRepository::findProductsByCostLessThan)
//                        .orElse(productRepository.findAll()));
//        List<Product> selectedProducts = productRepository.findAllFiltered(usernameFilter.get(), minCostFilter.get(), maxCostFilter.get());

        model.addAttribute("products", products);
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
        model.addAttribute("product", productRepository.findById(id)
                .orElseThrow(() -> new PageNotFoundException("Product not found.")));
        return "product_form";
    }

    @PostMapping
    public String update(@Valid Product product, BindingResult result) {
        logger.info("Saving product");

        if(result.hasErrors())
            return "product_form";

        productRepository.save(product);
        return "redirect:/product";
    }

    @GetMapping("/delete_{id}")
    public String deleteProduct(@PathVariable("id") Long id, Model model) {
        logger.info("Deleting product");
        //productRepository.delete(id);
        model.addAttribute("product", productRepository.findById(id)
                .orElseThrow(() -> new PageNotFoundException("Product not found.")));

        return "delete_form";
    }

    @GetMapping("/confirmed_deletion")
    public String confirmedDelete(Long id) {
        logger.info("Confirmed deleting product - id" + id);

        productRepository.deleteById(id);

        return "redirect:/product";
    }

    @ExceptionHandler
    public ModelAndView pageNotFoundExceptionHandler(PageNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

}
