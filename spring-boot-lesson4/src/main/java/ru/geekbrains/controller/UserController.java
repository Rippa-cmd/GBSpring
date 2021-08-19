package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.geekbrains.persist.User;
import ru.geekbrains.service.UserSearchFilters;
import ru.geekbrains.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private Optional<Integer> defaultPageSize = Optional.of(3);

    private Optional<Integer> defaultPagesCount = Optional.of(1);

    @Autowired
    private UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listPage(Model model, UserSearchFilters userSearchFilters) {
        logger.info("User list page requested");

        model.addAttribute("users", userService.findWithFilters(userSearchFilters));
        return "users";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        logger.info("New user page requested");

        model.addAttribute("userDto", new UserDto());
        return "user_form";
    }

    @GetMapping("/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("userDto", userService.findById(id)
                .orElseThrow(() -> new PageNotFoundException("User not found.")));
        return "user_form";
    }

    @PostMapping
    public String update(@Valid UserDto userDto, BindingResult result) {
        logger.info("Saving user");

        if (result.hasErrors())
            return "user_form";

        if (!userDto.checkPasswords()) {
            result.rejectValue("matchPassword", "matchPassword", "Пароли не совпадают");
            return "user_form";
        }

        userService.save(userDto);
        return "redirect:/user";
    }

    @GetMapping("/delete_{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        logger.info("Deleting user");
        //productRepository.delete(id);
        model.addAttribute("user", userService.findById(id)
                .orElseThrow(() -> new PageNotFoundException("User not found.")));

        return "user_delete_form";
    }

    @GetMapping("/confirmed_deletion")
    public String confirmedDelete(Long id) {
        logger.info("Confirmed deleting user - id" + id);

        userService.deleteById(id);

        return "redirect:/user";
    }

    @ExceptionHandler
    public ModelAndView pageNotFoundExceptionHandler(PageNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

}
