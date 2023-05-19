package com.konstantion.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public record ViewController() {
    @GetMapping("home")
    public String home() {
        return "home";
    }
}
