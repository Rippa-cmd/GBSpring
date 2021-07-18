package ru.geekbrains.service;

import ru.geekbrains.entity.Product;
import ru.geekbrains.entity.User;
import ru.geekbrains.entity.UserOrder;

import java.util.List;
import java.util.stream.Collectors;


public class OrderService {

    private DBExecutorBySessionFactory sfController;

    public OrderService(DBExecutorBySessionFactory sfController) {
        this.sfController = sfController;
    }

    public void buyProduct(Long productId, Long userId) {
        sfController.executeInTransaction(em -> {
            Product product = em.find(Product.class, productId);
            User user = em.find(User.class, userId);
            UserOrder order = new UserOrder(user, product);
            em.persist(order);
        });
    }

    public List<Product> getListOfBoughtProducts(Long userId) {
        return sfController.executeForEntityManager(em -> {
            User user = em.find(User.class, userId);
            return user.getUsersOrders().stream().map(UserOrder::getProduct).collect(Collectors.toList());
        });
    }

    public List<User> getListOfBuyersOfProduct(Long productId) {
        return sfController.executeForEntityManager(em -> {
            Product product = em.find(Product.class, productId);
            return product.getUsersOrders().stream().map(UserOrder::getUser).collect(Collectors.toList());
        });
    }

    public Integer getCostOfBoughtProducts(Long userId) {
        return getListOfBoughtProducts(userId).stream().mapToInt(Product::getPrice).sum();
    }

}
