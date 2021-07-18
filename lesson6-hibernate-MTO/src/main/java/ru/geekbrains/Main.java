package ru.geekbrains;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.geekbrains.entity.ProductDao;
import ru.geekbrains.entity.UserDao;
import ru.geekbrains.service.OrderService;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        OrderService orderService = context.getBean("orderService", OrderService.class);

        ProductDao productDao = context.getBean("productDao", ProductDao.class);

        UserDao userDao = context.getBean("userDao", UserDao.class);

        System.out.println("Список купленных товаров у пользователей:");

        userDao.findAll().forEach(user -> {
            System.out.println(user);
            orderService.getListOfBoughtProducts(user.getId()).forEach(System.out::println);
        });

        System.out.println("\nСписок пользователей купивших товар:");

        productDao.findAll().forEach(product -> {
            System.out.println(product);
            orderService.getListOfBuyersOfProduct(product.getId()).forEach(System.out::println);
        });

        System.out.println("\nОбщая затраченная сумма покупок:");

        userDao.findAll().forEach(user -> {
            System.out.println(user + " - " + orderService.getCostOfBoughtProducts(user.getId()));
        });
    }
}
