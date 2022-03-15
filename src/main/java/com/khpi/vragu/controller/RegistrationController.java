package com.khpi.vragu.controller;

import com.khpi.vragu.domain.Role;
import com.khpi.vragu.domain.User;
import com.khpi.vragu.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String showRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam String username, @RequestParam String password, Model model) {
        User userFromDb = userRepo.findByUsername(username);

        if (userFromDb != null) {
            model.addAttribute("message", "User Have Already Exist");
            return "registration";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:login";
    }
}
