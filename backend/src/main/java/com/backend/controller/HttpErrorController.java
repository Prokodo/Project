package com.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HttpErrorController {
    @ResponseBody
    @GetMapping("/403")
    public String forbidden() {
        return "Error 403 - Access Denied";
    }
}
