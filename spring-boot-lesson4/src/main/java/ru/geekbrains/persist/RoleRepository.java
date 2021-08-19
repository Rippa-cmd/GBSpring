package ru.geekbrains.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.controller.RoleDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAll();

    Set<Role> findByName(String name);

    default Set<RoleDto> mapToRoleDto() {
        return findAll().stream()
                .map(role -> new RoleDto(role.getId(), role.getName()))
                .collect(Collectors.toSet());
    }
}
