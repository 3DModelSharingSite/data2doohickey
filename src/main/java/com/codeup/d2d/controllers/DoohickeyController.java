package com.codeup.d2d.controllers;

import com.codeup.d2d.models.Doohickey;
import com.codeup.d2d.models.User;
import com.codeup.d2d.repos.DoohickeyRepository;
import com.codeup.d2d.repos.UserRepository;
import com.codeup.d2d.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;


@Controller
public class DoohickeyController {
    private UserRepository userDao;
    private DoohickeyRepository doohickeyDao;
    private AuthenticationService authSvc;

    @Autowired
    public DoohickeyController(AuthenticationService authSvc, UserRepository users, DoohickeyRepository doohickeyDao) {
        this.authSvc = authSvc;
        this.userDao = users;
        this.doohickeyDao = doohickeyDao;
    }


    @GetMapping("/doohickeys")
    public String showAllModels(@PageableDefault(value=1) Pageable pageable, Model model) {
        Pageable pageable2 = new PageRequest(pageable.getPageNumber()-1,pageable.getPageSize());
        model.addAttribute("page",doohickeyDao.findAll(pageable2));
        return "doohickeys/index";
    }

    @GetMapping("/doohickeys/{id}")
    public String showDoohickey(@PathVariable Long id, Model model){
        Doohickey doohickey = doohickeyDao.findOne(id);
        model.addAttribute("doohickey", doohickey);
        model.addAttribute("title", doohickey.getTitle() + " by " + doohickey.getAuthor().getUsername());
        return "doohickeys/show";
    }



    @GetMapping("/doohickeys/{id}/edit")
    public String showEditDoohickeyForm(@PathVariable Long id, Model model){
        if(authSvc.getCurUser() == null){
            return "redirect:/login";
        }
        model.addAttribute("doohickey", doohickeyDao.findOne(id));
        model.addAttribute("action", "/doohickeys/"+id+"/edit");
        model.addAttribute("title", "Edit Doohickey");
        return "doohickeys/editCreate";
    }
    @PostMapping("/doohickeys/{id}/edit")
    public String EditDoohickey(@PathVariable Long id, @Valid Doohickey editedDoohickey,
                                        Errors validation,
                                        Model model){
        if(authSvc.getCurUser() == null){
            return "redirect:/login";
        }
        if (validation.hasErrors()) {
            model.addAttribute("errors", validation);
            model.addAttribute("doohickey", editedDoohickey);
            model.addAttribute("action", "/doohickeys/"+id+"/edit");
            model.addAttribute("title", "Edit Doohickey");
            return "doohickeys/editCreate";
        }
        Doohickey doohickey = doohickeyDao.findOne(editedDoohickey.getId());


        doohickey.setTitle(editedDoohickey.getTitle());
        doohickey.setDescription(editedDoohickey.getDescription());


        doohickeyDao.save(doohickey);
        return "redirect:/doohickeys";
    }

    @GetMapping("/doohickeys/create")
    public String showCreateDoohickeyForm(Model model){
        if(authSvc.getCurUser() == null){
            return "redirect:/login";
        }
        model.addAttribute("doohickey", new Doohickey());
        model.addAttribute("action", "/doohickeys/create");
        model.addAttribute("title", "Create a Doohickey");
        return "doohickeys/editCreate";
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
            model.addAttribute("action", "/doohickeys/create");
            model.addAttribute("title", "Create a Doohickey");
            return "doohickeys/editCreate";
        }
        doohickey.setAuthor((User)authSvc.getCurUser());
        doohickey.setViews(0);
        doohickey.setDownloads(0);
        doohickeyDao.save(doohickey);
        return "redirect:/doohickeys";
    }

}