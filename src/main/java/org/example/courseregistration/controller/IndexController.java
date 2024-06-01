package org.example.courseregistration.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String remoteUser = authentication.getName(); // Retrieve the username
        model.addAttribute("remoteUser", remoteUser);
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
