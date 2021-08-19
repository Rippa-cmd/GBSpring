package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.controller.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Page<UserDto> findWithFilters(UserSearchFilters userSearchFilters);

    Optional<UserDto> findById(Long id);

    boolean isUsernameBusy(String username);

    void save(UserDto UserDto);

    void deleteById(Long id);

    List<UserDto> findAll();
}
