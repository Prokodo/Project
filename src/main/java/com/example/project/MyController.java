package com.example.project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MyController {

    @GetMapping("/")
    public String index(Model model) {
        String name = "Dominik";
        List<String> users = new ArrayList<>();
        users.add("Karel");
        users.add("Martin");
        users.add("Bob");

        model.addAttribute("users", users);
        model.addAttribute("name",name);

        return "main";
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello world";
    }

}
