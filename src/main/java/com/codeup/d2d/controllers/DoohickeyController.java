package com.codeup.d2d.controllers;

import com.codeup.d2d.models.Doohickey;
import com.codeup.d2d.models.User;
import com.codeup.d2d.repos.DoohickeyRepository;
import com.codeup.d2d.repos.UserRepository;
import com.codeup.d2d.services.AuthenticationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@Controller
public class DoohickeyController {
    private UserRepository userDao;
    private DoohickeyRepository doohickeyDao;
    private AuthenticationService authSvc;

    public DoohickeyController(AuthenticationService authSvc, UserRepository users, DoohickeyRepository doohickeyDao) {
        this.authSvc = authSvc;
        this.userDao = users;
        this.doohickeyDao = doohickeyDao;
    }


    @GetMapping("/doohickeys")
    public String showAllModels(@RequestParam(defaultValue = "1") String page, Model model) {
        System.out.println(page);
        int pageInt = Integer.parseInt(page)-1;
        PageRequest pagerequest = new PageRequest(pageInt,30);
        Page<Doohickey> doohickeyList = doohickeyDao.findAll(pagerequest);

        model.addAttribute("doohickeyList",doohickeyList);
        return "doohickeys/index";
    }

    @GetMapping("/doohickeys/create")
    public String showCreateDoohickeyForm(Model model){
        if(authSvc.getCurUser() == null){
            return "redirect:/login";
        }
        model.addAttribute("doohickey", new Doohickey());
        model.addAttribute("action", "/doohickeys/create");
        model.addAttribute("title", "Create a Doohickey");
        return "doohickeys/create";
    }

    @PostMapping("/doohickeys/create")
    public String CreateDoohickey(@Valid Doohickey doohickey,
                           Errors validation,
                           Model model) {
        if(authSvc.getCurUser() == null){
            return "redirect:/login";
        }
        if (validation.hasErrors()) {
            model.addAttribute("errors", validation);
            model.addAttribute("doohickey", doohickey);
            return "users/sign-up";
        }
        doohickey.setAuthor((User)authSvc.getCurUser());
        doohickey.setViews(0);
        doohickey.setDownloads(0);
        doohickeyDao.save(doohickey);
        return "redirect:/doohickeys";
    }

}