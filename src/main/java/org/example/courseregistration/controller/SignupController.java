package org.example.courseregistration.controller;


import jakarta.validation.Valid;
import org.example.courseregistration.entity.User;
import org.example.courseregistration.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class SignupController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "signup")
    public String signup(Model model) {
        model.addAttribute("signup", new User());
        return "signup";
    }

    @RequestMapping(value = "saveSignup", method = RequestMethod.POST)
    public String saveSignup(@Valid @ModelAttribute("signup") User user, BindingResult bindingResult, RedirectAttributes redirAttrs) {
        if (!bindingResult.hasErrors()) { // validation errors
            if (user.getPassword().equals(user.getPasswordCheck())) { // check password match
                if (userService.findByUsername(user.getUsername()) == null) { // validate username
                    // encrypt password
                    userService.encryptPassword(user);
                    user.setRole("USER");
                    userService.saveUser(user);
                    redirAttrs.addFlashAttribute("message", "User registered successfully!");
                    return "redirect:/login";
                } else {
                    bindingResult.rejectValue("username", "error.userexists", "Username already exists");
                }
            } else {
                bindingResult.rejectValue("passwordCheck", "error.pwdmatch", "Passwords does not match");
            }
        }
        return "signup";
    }

}
