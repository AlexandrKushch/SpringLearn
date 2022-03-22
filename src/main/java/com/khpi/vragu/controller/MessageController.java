package com.khpi.vragu.controller;

import com.khpi.vragu.domain.Message;
import com.khpi.vragu.domain.User;
import com.khpi.vragu.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class MessageController {
    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

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
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException
    {
        Message message = new Message(text, tag, author);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File dir = new File(uploadPath);

            if (!dir.exists()) {
                dir.mkdir();
            }

            String filename = UUID.randomUUID() + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + filename));

            message.setFilename(filename);
        }

        messageRepo.save(message);

        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);

        return "messages";
    }
}
