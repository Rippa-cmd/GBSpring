package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.geekbrains.controller.RoleDto;
import ru.geekbrains.controller.UserDto;
import ru.geekbrains.persist.Role;
import ru.geekbrains.persist.User;
import ru.geekbrains.persist.UserRepository;
import ru.geekbrains.persist.UserSpecifications;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<UserDto> findWithFilters(UserSearchFilters userSearchFilters) {
        Specification<User> spec = Specification.where(null);
        Sort sortConfiguration;
        if (userSearchFilters.getUsernameFilter() != null && !userSearchFilters.getUsernameFilter().isBlank())
            spec = spec.and(UserSpecifications.usernamePrefix(userSearchFilters.getUsernameFilter()));

        if (userSearchFilters.getMinAgeFilter() != null)
            spec = spec.and(UserSpecifications.minAge(userSearchFilters.getMinAgeFilter()));

        if (userSearchFilters.getMaxAgeFilter() != null)
            spec = spec.and(UserSpecifications.maxAge(userSearchFilters.getMaxAgeFilter()));

        if (userSearchFilters.getSize() == null || userSearchFilters.getSize() <= 0)
            userSearchFilters.setSize(3);

        if (userSearchFilters.getPage() == null || userSearchFilters.getPage() <= 0)
            userSearchFilters.setPage(1);

        if (userSearchFilters.getSortField() != null && !userSearchFilters.getSortField().isBlank())
            sortConfiguration = Sort.by(userSearchFilters.getSortField());
        else
            sortConfiguration = Sort.by("id");

        if ("desc".equals(userSearchFilters.getSortDirection()))
            sortConfiguration = sortConfiguration.descending();

        return userRepository.findAll(spec, PageRequest.of(userSearchFilters.getPage() - 1,
                userSearchFilters.getSize(), sortConfiguration)).
                map(user -> new UserDto(user.getId(),
                        user.getUsername(),
                        user.getAge(),
                        mapRolesDto(user)));
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDto(user.getId(), user.getUsername(), user.getAge(), mapRolesDto(user)));
    }

    @Override
    public boolean isUsernameBusy(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public void save(UserDto userDto) {
        User user = new User(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getAge(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getRoles().stream().
                        map(role -> new Role(role.getId(), role.getName()))
                        .collect(Collectors.toSet()));
        userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDto(user.getId(), user.getUsername(), user.getAge(), mapRolesDto(user))).collect(Collectors.toList());
    }

    private Set<RoleDto> mapRolesDto(User user) {
        return user.getRoles().stream()
                .map(role -> new RoleDto(role.getId(), role.getName()))
                .collect(Collectors.toSet());
    }
}
