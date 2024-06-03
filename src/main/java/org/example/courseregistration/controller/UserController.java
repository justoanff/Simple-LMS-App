package org.example.courseregistration.controller;

import jakarta.validation.Valid;
import org.example.courseregistration.entity.User;
import org.example.courseregistration.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "saveUser", method = RequestMethod.POST)
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam(value = "id", required = false) Long userId, Model model) {
        if (bindingResult.hasErrors()) { // validation errors
            String title = (userId == null) ? "Add User" : "Edit User";
            model.addAttribute("title", title);
            return "userForm";
        }

        try {
            if (user.getPassword() != null) {
                // encrypt password
                userService.encryptPassword(user);
            }

            if (userId == null) { // check if new user
                User existingUser = userService.findByUsername(user.getUsername());
                if (existingUser != null) { // validate username
                    bindingResult.rejectValue("username", "error.userexists", "Username already exists");
                    model.addAttribute("title", "Add User");
                    return "userForm";
                }
            }

            userService.saveUser(user);
            return "redirect:/users";
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("username", "error.userexists", "Username already exists");
            model.addAttribute("title", "Add User");
            return "userForm";
        }
    }

    @RequestMapping("users")
    public String index(Model model) {
        List<User> users = (List<User>) userService.findAllByOrderByUsernameAsc();
        model.addAttribute("users", users);
        return "users";
    }

    @RequestMapping("user/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("title", "Add User");
        return "userForm";
    }

    @RequestMapping(value = "user/edit/{id}")
    public String editUser(@PathVariable("id") Long userId, Model model) {
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("title", "Edit User");
        return "userForm";
    }

    @RequestMapping("user/{id}")
    public String showUser(@PathVariable("id") Long userId, Model model) {
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("title", "Show User");
        return "userShow";
    }

    @RequestMapping(value = "user/delete/{id}", method = RequestMethod.GET)
    public String deleteUser(@PathVariable("id") Long userId, Model model) {
        userService.deleteUserById(userId);
        return "redirect:/users";
    }
}