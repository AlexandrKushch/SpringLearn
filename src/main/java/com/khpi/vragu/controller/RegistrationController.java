package com.khpi.vragu.controller;

import com.khpi.vragu.domain.Role;
import com.khpi.vragu.domain.User;
import com.khpi.vragu.repos.UserRepo;
import com.khpi.vragu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String showRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        if (!userService.addUser(user)) {
            model.addAttribute("message", "User Have Already Exist");
            return "registration";
        }

        return "redirect:login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated!");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }
}
