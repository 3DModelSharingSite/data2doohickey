package com.codeup.d2d.controllers;

import com.codeup.d2d.models.User;
import com.codeup.d2d.models.UserDetails;
import com.codeup.d2d.models.UserRole;
import com.codeup.d2d.repos.UserDetailsRepository;
import com.codeup.d2d.repos.UserRepository;
import com.codeup.d2d.repos.UserRoles;
import com.codeup.d2d.services.AuthenticationService;
import com.codeup.d2d.services.HaveIBeenPwndService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {
    private UserRepository userDao;
    private UserDetailsRepository userDetailsDao;
    private PasswordEncoder passwordEncoder;
    private HaveIBeenPwndService hibp;

    @Autowired
    private UserRoles userRoles;

    private AuthenticationService authSvc;

    public UserController(HaveIBeenPwndService hibp, UserDetailsRepository userDetailsDao,AuthenticationService authSvc, UserRepository users, PasswordEncoder passwordEncoder) {
        this.authSvc = authSvc;
        this.userDao = users;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsDao = userDetailsDao;
        this.hibp = hibp;
    }

    @GetMapping("/profile")
    public String showProfile(){
        if(authSvc.getCurUser() == null){
            return "redirect:/login";
        }
        return "redirect:/profile/"+((User)authSvc.getCurUser()).getUsername();
    }
    @GetMapping("/profile/{username}")
    public String showProfileOfUser(@PathVariable String username, Model model){
        User user = userDao.findByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("userDetails",user.getUserDetails());
        return "users/profile";
    }

    @GetMapping("/profile/{username}/edit")
    public String editProfileDetailsOfUser(@PathVariable String username, Model model){
        User user = userDao.findByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("userDetails",user.getUserDetails());
        return "users/edit";
    }
    @PostMapping("/profile/{username}/edit")
    public String saveProfileDetailsOfUser(@PathVariable String username,
                           @Valid UserDetails userDetails,
                           Errors validation,
                           Model model){
        User user = userDao.findByUsername(username);
        UserDetails oldDetails = user.getUserDetails();

        System.out.println(oldDetails.getBio());
        System.out.println(userDetails.getBio());
        oldDetails.setBio(userDetails.getBio());
        oldDetails.setClassification(userDetails.getClassification());
        oldDetails.setLocation(userDetails.getLocation());

        userDetailsDao.save(oldDetails);
        return "redirect:/profile/"+oldDetails.getUser().getUsername();
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
        if(user.getCnfmpassword().equals("")){
            validation.rejectValue("password",null,"You must confirm your password!");
        }
        if(hibp.CheckPasswordForPwnage(user.getPassword())){
            validation.rejectValue("password",null,
                    "This password has been compromised in the past!");
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

        UserDetails userDetails = new UserDetails("",3,"");
        userDetails.setUser(user);
        user.setUserDetails(userDetails);

        User newUser = userDao.save(user);

        UserRole ur = new UserRole();
        ur.setRole("ROLE_USER");
        ur.setUserId(newUser.getId());
        userRoles.save(ur);

        authSvc.authenticate(user);
        model.addAttribute("user", user);

        return "redirect:/";
    }

    @GetMapping("/profile/{username}/favorites")
    public String userFavorites(@PathVariable String username, Model model){
        User user = userDao.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("userDetails", user.getFavorites());
        return "users/favorites";
    }

    @GetMapping("/profile/{username}/doohickeys")
    public String userDoohickeys(@PathVariable String username, Model model){
        User user = userDao.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("userDetails", user.getDoohickeyList());
        return "users/doohickeys";
    }
    @PostMapping("/profile/updatePicture")
    public String updatePFP(@RequestParam String key){
        if(authSvc.getCurUser() == null){
            return "redirect:/login";
        }
        User user = (User)authSvc.getCurUser();
        user = userDao.findOne(user.getId());
        user.setPhotoURL(key);
        userDao.save(user);
        return "redirect:/profile";
    }
}