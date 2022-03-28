package com.khpi.vragu.controller;

import com.khpi.vragu.domain.Role;
import com.khpi.vragu.domain.User;
import com.khpi.vragu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String showUserList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{id}")
    public String showEditUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String editUser(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam Long userId,
            Model model
    ) {
        userService.editUser(username, form, userId);

        return "redirect:/users";
    }

    @GetMapping("profile")
    public String showProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password
    ) {
        userService.updateProfile(user, username, email, password);

        return "redirect:/users/profile";
    }
}
