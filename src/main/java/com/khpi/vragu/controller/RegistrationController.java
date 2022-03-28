package com.khpi.vragu.controller;

import com.khpi.vragu.domain.User;
import com.khpi.vragu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String showRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfirm,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
        boolean isPasswordConfirmedNull = passwordConfirm == null || passwordConfirm.isEmpty();

        if (isPasswordConfirmedNull) {
            model.addAttribute("password2Error", "Password confirmation can not be empty");
        }
        if (isPasswordConfirmedNull || bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);

            model.addAttribute("errorMap", errorMap);
            model.addAttribute("user", user);
            return "registration";
        }
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("password2Error", "Passwords are different");

            model.addAttribute("user", user);
            return "registration";
        }
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User Have Already Exist");
            return "registration";
        }

        model.addAttribute("user", null);
        return "redirect:login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated!");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }
}
