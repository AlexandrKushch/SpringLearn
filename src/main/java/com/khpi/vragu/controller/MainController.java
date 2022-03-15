package com.khpi.vragu.controller;

import com.khpi.vragu.domain.Message;
import com.khpi.vragu.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String showMessages(Model model) {
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);

        return "messages";
    }

    @PostMapping(value = "/messages")
    public String addNewMessage(@RequestParam String text, @RequestParam String tag, Model model) {
        Message message = new Message(text, tag);
        messageRepo.save(message);

        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);

        return "messages";
    }

    @PostMapping(value = "/filter")
    public String filterMessages(@RequestParam String filter, Model model) {
        if (filter.equals("")) {
            return showMessages(model);
        } else {
            List<Message> messages = messageRepo.findByTag(filter);
            model.addAttribute("messages", messages);
        }

        return "messages";
    }
}
