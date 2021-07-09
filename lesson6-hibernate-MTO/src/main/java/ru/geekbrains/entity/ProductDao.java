package ru.geekbrains.entity;

import ru.geekbrains.service.DBExecutorBySessionFactory;

import java.util.List;

/**
 * A class for working with a database with entities of the type Product.class
 */
public class ProductDao {
    private DBExecutorBySessionFactory sfController;

    public ProductDao(DBExecutorBySessionFactory sfController) {
        this.sfController = sfController;
    }

    /**
     * Finds product by id
     *
     * @param id identity of entity
     * @return product, if exist, or null if not
     */
    public Product findById(Long id) {
        return sfController.executeForEntityManager(em -> em.find(Product.class, id));
    }

    /**
     * Finds all products
     *
     * @return list of exists products
     */
    public List<Product> findAll() {
        return sfController.executeForEntityManager(em -> em.createNamedQuery("allProducts", Product.class).getResultList());
    }

    /**
     * Delete record from DB, if exist
     *
     * @param id identity of entity
     */
    public void deleteById(Long id) {
        sfController.executeInTransaction(em -> em.createQuery("delete from Product p where p.id = :id")
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
            sfController.executeInTransaction(em -> em.persist(product));
            // иначе обновляем
        else
            sfController.executeInTransaction(em -> em.merge(product));

        return oldProduct;
    }
}
