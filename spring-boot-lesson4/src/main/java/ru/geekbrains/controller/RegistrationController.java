package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.geekbrains.persist.RoleRepository;
import ru.geekbrains.service.UserService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    private RegistrationController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }


    @GetMapping()
    public String registrationPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "registration_page";
    }

    @PostMapping
    public String update(@Valid UserDto userDto, BindingResult result, Model model) {
        logger.info("Saving user");

        if (result.hasErrors()) {
            model.addAttribute("roles", roleRepository.mapToRoleDto());
            return "registration_page";
        }

        if (userService.isUsernameBusy(userDto.getUsername())) {
            model.addAttribute("roles", roleRepository.mapToRoleDto());
            result.rejectValue("username", "username", "Логин уже занят");
            return "registration_page";
        }

        if (!userDto.checkPasswords()) {
            model.addAttribute("roles", roleRepository.mapToRoleDto());
            result.rejectValue("matchPassword", "matchPassword", "Пароли не совпадают");
            return "registration_page";
        }

        userDto.setRoles(roleRepository.findByName("ROLE_GUEST")
                .stream()
                .map(role -> new RoleDto(role.getId(), role.getName()))
                .collect(Collectors.toSet()));

        userService.save(userDto);
        return "redirect:/login";
    }
}
