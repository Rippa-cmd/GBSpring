package ru.geekbrains.entity;

import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A class for working with a database with entities of the type Product.class
 */
public class ProductDao {
    private EntityManagerFactory emFactory;

    public ProductDao(EntityManagerFactory emFactory) {
        this.emFactory = emFactory;
    }

    /**
     * Finds product by id
     *
     * @param id identity of entity
     * @return product, if exist, or null if not
     */
    public Product findById(Long id) {
        return executeForEntityManager(em -> em.find(Product.class, id));
    }

    /**
     * Finds all products
     *
     * @return list of exists products
     */
    public List<Product> findAll() {
        return executeForEntityManager(em -> em.createNamedQuery("allProducts", Product.class).getResultList());
    }

    /**
     * Delete record from DB, if exist
     *
     * @param id identity of entity
     */
    public void deleteById(Long id) {
        executeInTransaction(em -> em.createQuery("delete from Product p where p.id = :id")
                .setParameter("id", id));
    }

    /**
     * Creates or updates a product record
     *
     * @param product entity to be inserted
     * @return old product, if exist, or null if not
     */
    public Product saveOrUpdate(Product product) {
        Long productId = product.getId();
        Product oldProduct = null;

        // проверяем, существует ли сущность с данным id
        if (productId != null)
            oldProduct = findById(productId);

        // если id свободен, создаем запись
        if (oldProduct == null)
            executeInTransaction(em -> em.persist(product));
            // иначе обновляем
        else
            executeInTransaction(em -> em.merge(product));

        return oldProduct;
    }

    private <R> R executeForEntityManager(Function<EntityManager, R> function) {
        EntityManager em = emFactory.createEntityManager();
        try {
            return function.apply(em);
        } finally {
            em.close();
        }
    }

    private void executeInTransaction(Consumer<EntityManager> consumer) {
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
