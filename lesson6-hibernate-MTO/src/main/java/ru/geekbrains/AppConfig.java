package ru.geekbrains;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.geekbrains.entity.ProductDao;
import ru.geekbrains.entity.UserDao;
import ru.geekbrains.service.DBExecutorBySessionFactory;
import ru.geekbrains.service.OrderService;

@Configuration
public class AppConfig {

    @Bean(name = "DBExecutorBySessionFactory")
    public DBExecutorBySessionFactory DBExecutorBySessionFactory() {
        return new DBExecutorBySessionFactory();
    }

    @Bean
    public UserDao userDao() {
        return new UserDao(DBExecutorBySessionFactory());
    }

    @Bean
    public ProductDao productDao() {
        return new ProductDao(DBExecutorBySessionFactory());
    }

    @Bean
    public OrderService orderService() {
        return new OrderService(DBExecutorBySessionFactory());
    }
}
