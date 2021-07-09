package ru.geekbrains.entity;

import ru.geekbrains.service.DBExecutorBySessionFactory;

import java.util.List;

public class UserDao {

    private DBExecutorBySessionFactory sfController;

    public UserDao(DBExecutorBySessionFactory sfController) {
        this.sfController = sfController;
    }

    /**
     * Finds user by id
     *
     * @param id identity of entity
     * @return user, if exist, or null if not
     */
    public User findById(Long id) {
        return sfController.executeForEntityManager(em -> em.find(User.class, id));
    }

    /**
     * Finds all users
     *
     * @return list of exists users
     */
    public List<User> findAll() {
        return sfController.executeForEntityManager(em -> em.createNamedQuery("allUsers", User.class).getResultList());
    }

    /**
     * Delete record from DB, if exist
     *
     * @param id identity of entity
     */
    public void deleteById(Long id) {
        sfController.executeInTransaction(em -> em.createQuery("delete from User u where u.id = :id")
                .setParameter("id", id));
    }

    /**
     * Creates or updates a user record
     *
     * @param user entity to be inserted
     * @return old user, if exist, or null if not
     */
    public User saveOrUpdate(User user) {
        Long userId = user.getId();
        User oldUser = null;

        // проверяем, существует ли сущность с данным id
        if (userId != null)
            oldUser = findById(userId);

        // если id свободен, создаем запись
        if (oldUser == null)
            sfController.executeInTransaction(em -> em.persist(user));
            // иначе обновляем
        else
            sfController.executeInTransaction(em -> em.merge(user));

        return oldUser;
    }
}
