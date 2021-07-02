package ru.geekbrains.entity;

import org.hibernate.cfg.Configuration;
import ru.geekbrains.entity.Product;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * A class for working with a database with entities of the type Product.class
 */
public class ProductDao {
    private EntityManager em;

    public ProductDao() {
        em = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory().createEntityManager();
    }

    /**
     * Finds product by id
     * @param id identity of entity
     * @return product, if exist, or null if not
     */
    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    /**
     * Finds all products
     * @return list of exists products
     */
    public List<Product> findAll() {
        return em.createNamedQuery("allProducts", Product.class).getResultList();
    }

    /**
     * Delete record from DB, if exist
     * @param id identity of entity
     */
    public void deleteById(Long id) {
        em.getTransaction().begin();
        Product product = em.find(Product.class, id);
        if (product != null)
            em.remove(product);
    }

    /**
     * Creates or updates a product record
     * @param product entity to be inserted
     * @return old product, if exist, or null if not
     */
    public Product saveOrUpdate(Product product) {
        Long productId = product.getId();
        Product oldProduct = null;

        // проверяем, существует ли сущность с данным id
        if (productId != null)
            oldProduct = em.find(Product.class, productId);

        em.getTransaction().begin();
        {
            // если id свободен, создаем запись
            if (oldProduct == null)
                em.persist(product);
            // иначе обновляем
            else
                em.merge(product);
        }
        em.getTransaction().commit();

        return oldProduct;
    }

    public void close() {
        em.close();
    }
}
