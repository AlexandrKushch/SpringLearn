package com.khpi.vragu.controller;

import com.khpi.vragu.domain.Role;
import com.khpi.vragu.domain.User;
import com.khpi.vragu.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String showUserList(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "userList";
    }

    @GetMapping("{id}")
    public String showEditUser(@PathVariable Long id, Model model) {
        User user = userRepo.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String editUser(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam Long userId
    ) {
        User user = userRepo.findById(userId);


        Set<Role> roles = Arrays.stream(Role.values())
                .filter(r -> form.containsKey(r.name()))
                .collect(Collectors.toSet());

        user.setRoles(roles);

        user.setUsername(username);
        userRepo.save(user);

        return "redirect:/user";
    }
}
