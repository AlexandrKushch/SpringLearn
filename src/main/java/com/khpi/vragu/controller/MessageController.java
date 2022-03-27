package com.khpi.vragu.controller;

import com.khpi.vragu.domain.Message;
import com.khpi.vragu.domain.User;
import com.khpi.vragu.repos.MessageRepo;
import com.khpi.vragu.service.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class MessageController {
    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private EmailSender emailSender;

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
            @Valid Message message,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException
    {
        message.setAuthor(author);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            System.out.println("Has errors!");

            model.addAttribute("errorMap", errorMap);
            model.addAttribute("message", message);
        } else {
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                File dir = new File(uploadPath);

                if (!dir.exists()) {
                    dir.mkdir();
                }

                String filename = UUID.randomUUID() + "." + file.getOriginalFilename();

                file.transferTo(new File(uploadPath + filename));

                message.setFilename(filename);
            }
            model.addAttribute("message", null);

            messageRepo.save(message);
        }

        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "messages";
    }
}
