package com.khpi.vragu.controller;

import com.khpi.vragu.domain.Message;
import com.khpi.vragu.domain.User;
import com.khpi.vragu.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping(value = "/")
    public String showGreeting(Model model) {
        return "home";
    }

    @GetMapping(value = "/messages")
    public String showMessages(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages;

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "messages";
    }

    @PostMapping(value = "/messages")
    public String addNewMessage(
            @AuthenticationPrincipal User author,
            @RequestParam String text,
            @RequestParam String tag,
            Model model)
    {
        Message message = new Message(text, tag, author);
        messageRepo.save(message);

        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);

        return "messages";
    }
}
