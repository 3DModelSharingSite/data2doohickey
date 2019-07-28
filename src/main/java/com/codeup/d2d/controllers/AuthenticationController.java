package com.codeup.d2d.controllers;

import com.codeup.d2d.models.User;
import com.codeup.d2d.services.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
    private AuthenticationService authSvc;

    public AuthenticationController(AuthenticationService authSvc){
        this.authSvc = authSvc;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        if(authSvc.getCurUser() != null){
            return "redirect:/profile";
        }
        return "users/login";
    }

}