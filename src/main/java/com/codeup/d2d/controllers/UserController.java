package com.codeup.d2d.controllers;

import com.codeup.d2d.models.User;
import com.codeup.d2d.models.UserRole;
import com.codeup.d2d.repos.UserRepository;
import com.codeup.d2d.repos.UserRoles;
import com.codeup.d2d.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
public class UserController {
    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoles userRoles;

    private AuthenticationService authSvc;

    public UserController(AuthenticationService authSvc, UserRepository users, PasswordEncoder passwordEncoder) {
        this.authSvc = authSvc;
        this.userDao = users;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String showProfile(Model model){
        if(authSvc.getCurUser() == null){
            return "redirect:/login";
        }
        model.addAttribute("user",userDao.findOne(((User)authSvc.getCurUser()).getId()));
        return "users/profile";
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        if(authSvc.getCurUser() != null){
            return "redirect:/profile";
        }
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@Valid User user,
                           Errors validation,
                           Model model){
        if(authSvc.getCurUser() != null){
            return "redirect:/profile";
        }
        if(userDao.findByEmail(user.getEmail()) != null){
            validation.rejectValue("email",null,"This email is already in use!");
        }
        if(userDao.findByUsername(user.getUsername()) != null){
            validation.rejectValue("username",null,"This username already exists!");
        }
        if(!user.getCnfmpassword().equals(user.getPassword())){
            validation.rejectValue("password",null,
                    "The passwords must match!");
        }
        if (validation.hasErrors()) {
            model.addAttribute("errors", validation);
            model.addAttribute("user", user);
            return "users/sign-up";
        }
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        user.setEnabled(true);

        User newUser = userDao.save(user);

        UserRole ur = new UserRole();
        ur.setRole("ROLE_USER");
        ur.setUserId(newUser.getId());
        userRoles.save(ur);

        authSvc.authenticate(user);

        model.addAttribute("user", user);

        return "redirect:/";
    }
}