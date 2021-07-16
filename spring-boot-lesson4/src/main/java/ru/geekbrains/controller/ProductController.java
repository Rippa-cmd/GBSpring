package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.geekbrains.persist.Product;
import ru.geekbrains.persist.ProductRepository;
import ru.geekbrains.persist.ProductSpecifications;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductRepository productRepository;

    private Optional<Integer> defaultPageSize = Optional.of(3);

    private Optional<Integer> defaultPagesCount = Optional.of(1);

    @Autowired
    private ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String listPage(Model model,
                           @RequestParam("productNameFilter") Optional<String> productNameFilter,
                           @RequestParam("minCostFilter") Optional<BigDecimal> minCostFilter,
                           @RequestParam("maxCostFilter") Optional<BigDecimal> maxCostFilter,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size,
                           @RequestParam("sort") Optional<String> sort) {
        logger.info("Product list page requested");

        Specification<Product> spec = Specification.where(null);
        Sort sortConfiguration;
        if (productNameFilter.isPresent() && !productNameFilter.get().isBlank())
            spec = spec.and(ProductSpecifications.productNamePrefix(productNameFilter.get()));

        if (minCostFilter.isPresent())
            spec = spec.and(ProductSpecifications.minCost(minCostFilter.get()));

        if (maxCostFilter.isPresent())
            spec = spec.and(ProductSpecifications.maxCost(maxCostFilter.get()));

        if (size.isPresent()) {
            if (size.get() <= 0)
                size = defaultPageSize;
        } else size = defaultPageSize;

        if (page.isPresent()) {
            if (page.get() <= 0)
                page = defaultPagesCount;
        } else page = defaultPagesCount;

        if (sort.isPresent() && !sort.get().isBlank())
            sortConfiguration = Sort.by(sort.get());
        else
            sortConfiguration = Sort.by("id");

        model.addAttribute("products", productRepository.findAll(spec,
                PageRequest.of(page.get() - 1, size.get(), sortConfiguration)));
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

        if (result.hasErrors())
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
