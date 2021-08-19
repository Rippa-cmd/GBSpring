package ru.geekbrains.persist;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    List<User> findUsersByUsernameStartsWith(String prefix);

//    @Query(value = "select p from Product p where (p.name like CONCAT(:prefix, '%') or :prefix is null) " +
//            "and (p.cost >= :minCost or :minCost is null) " +
//            "and (p.cost <= :maxCost or :maxCost is null)")
//    List<Product> findAllFiltered(@Param("prefix") String prefix,
//                                  @Param("minCost") BigDecimal minCost,
//                                  @Param("maxCost") BigDecimal maxCost);

    @EntityGraph(value = "GroupInfo.detail", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByUsername(String username);
}
