package com.codeup.d2d.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class HomeController {

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

}