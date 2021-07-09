package ru.geekbrains.service;

import org.hibernate.cfg.Configuration;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.function.Consumer;
import java.util.function.Function;

public class DBExecutorBySessionFactory {

    private EntityManagerFactory emFactory;

    @PostConstruct
    public void init() {
        this.emFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    public void setEmFactory(EntityManagerFactory emFactory) {
        this.emFactory = emFactory;
    }

    public <R> R executeForEntityManager(Function<EntityManager, R> function) {
        EntityManager em = emFactory.createEntityManager();
        try {
            return function.apply(em);
        } finally {
            em.close();
        }
    }

    public void executeInTransaction(Consumer<EntityManager> consumer) {
        EntityManager em = emFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            consumer.accept(em);
            em.getTransaction().commit();
        } catch (Exception ignored) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
