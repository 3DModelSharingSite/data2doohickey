package com.codeup.d2d.controllers;

import com.codeup.d2d.models.Doohickey;
import com.codeup.d2d.models.File;
import com.codeup.d2d.models.Tag;
import com.codeup.d2d.models.User;
import com.codeup.d2d.repos.DoohickeyRepository;
import com.codeup.d2d.repos.FileRepository;
import com.codeup.d2d.repos.TagRepository;
import com.codeup.d2d.repos.UserRepository;
import com.codeup.d2d.services.AuthenticationService;
import com.codeup.d2d.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Controller
public class DoohickeyController {
    private final UserRepository userDao;
    private final TagRepository tagDao;
    private final DoohickeyRepository doohickeyDao;
    private final FileRepository fileDao;
    private final S3Service s3svc;
    private final AuthenticationService authSvc;

    @Autowired
    public DoohickeyController(S3Service s3svc, FileRepository fileDao, TagRepository tagDao, AuthenticationService authSvc, UserRepository users, DoohickeyRepository doohickeyDao) {
        this.authSvc = authSvc;
        this.userDao = users;
        this.doohickeyDao = doohickeyDao;
        this.tagDao=tagDao;
        this.fileDao=fileDao;
        this.s3svc = s3svc;
    }

    @GetMapping("/doohickeys")
    public String showAllDoohickeys(@RequestParam(required = false) String search, @PageableDefault(page = 1,size=3) Pageable pageable, Model model) {

        if(search == null) {
            search="";
        }
        model.addAttribute("search",search);
        if(search.equals("")){
            search = "%";
        }else{
            search = "%" + search + "%";
        }
        Pageable pageable2 = new PageRequest(pageable.getPageNumber()-1,pageable.getPageSize());

        model.addAttribute("page",doohickeyDao.findByTitleIsLikeOrDescriptionIsLikeOrderByIdDesc(search,search,pageable2));
        model.addAttribute("user", authSvc.getCurUser());

        return "doohickeys/doohickeys";
    }

    @GetMapping("/doohickeys/{id}/download")
    @ResponseBody
    public String downloadDoohickey(@PathVariable Long id, Model model){
        Doohickey doohickey = doohickeyDao.findOne(id);
        doohickey.setDownloads(doohickey.getDownloads()+1);
        doohickeyDao.save(doohickey);
        return "Downloaded";
    }

    @GetMapping("/doohickeys/{id}")
    public String showDoohickey(@PathVariable Long id, Model model){
        Doohickey doohickey = doohickeyDao.findOne(id);
        doohickey.setViews(doohickey.getViews()+1);
        doohickeyDao.save(doohickey);
        File file = fileDao.findByDoohickey_Id(id);
        model.addAttribute("file",file);
        model.addAttribute("doohickey", doohickey);
        model.addAttribute("title", doohickey.getTitle() + " by " + doohickey.getAuthor().getUsername());
        return "doohickeys/show";
    }

    @GetMapping("/doohickeys/{id}/favorite")
    @ResponseBody
    public String favoriteDoohickey(@PathVariable Long id){
        if(authSvc.getCurUser() == null){
            return "Failure";
        }
        User user = (User)authSvc.getCurUser();
        user = userDao.findOne(user.getId());
        Doohickey doohickey = doohickeyDao.findOne(id);
        if(user.getFavorites().contains(doohickey)){
            user.getFavorites().remove(doohickey);
            userDao.save(user);
            System.out.println(id+" Has been unfavorited!");
            return "UnFavorited";
        }else {
            user.getFavorites().add(doohickey);
            userDao.save(user);
            System.out.println(id+" Has been favorited!");
            return "Favorited";
        }
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
                           Model model,
                           @RequestParam String tagsString,
                           @RequestParam(required=false)List<String> keys) {
        if(authSvc.getCurUser() == null){
            return "redirect:/login";
        }
        if(keys == null || keys.size() < 1){
            validation.rejectValue("files",null,"A doohickey must have files!");
        }
        if(doohickey.getTagsString().equals("")){
            validation.rejectValue("tagsString",null,"A doohickey must have tags!");
        }
        if (validation.hasErrors()) {
            model.addAttribute("errors", validation);
            model.addAttribute("doohickey", doohickey);
            model.addAttribute("action", "/doohickeys/create");
            model.addAttribute("title", "Create a Doohickey");
            return "doohickeys/editCreate";
        }
        List<String> tagStrings = Arrays.asList(tagsString.toLowerCase().split("\\s*,\\s*"));
        List<Tag> tags = new ArrayList<>();
        for(String tag :tagStrings){
            System.out.println(tag);
            if(tagDao.findByName(tag) != null) {
                tags.add(tagDao.findByName(tag));
            }else{
                tags.add(new Tag(tag));
            }
        }

        doohickey.setTags(tags);
        doohickey.setAuthor((User)authSvc.getCurUser());
        doohickey.setViews(0);
        doohickey.setDownloads(0);
        doohickey = doohickeyDao.save(doohickey);

        for(String key : keys){
            boolean isModel=key.endsWith(".stl");
            String newKey = key.substring(4);
            s3svc.moveObject(key,"data2doohickey",
                    "data2doohickey",newKey);
            File file = new File(doohickey,newKey,isModel);
            fileDao.save(file);
        }

        return "redirect:/doohickeys/"+doohickey.getId();
    }

    @GetMapping("/doohickeys/{id}/3d")
    public String showDoohickey3D(@PathVariable Long id, Model model){
        File file = fileDao.findByDoohickey_Id(id);
        model.addAttribute("file",file);
        return "doohickeys/3dShow";
    }
    @PostMapping("/doohickeys/{id}/delete")
    public String deleteDoohickey(@PathVariable Long id, Model model){
        if(authSvc.getCurUser() == null){
            return "redirect:/login";
        }
        User logUser = (User)authSvc.getCurUser();
        User dhkyAuthor = doohickeyDao.findOne(id).getAuthor();
        if(logUser.getId() == dhkyAuthor.getId()) {
            doohickeyDao.delete(id);
        }
        return "redirect:/doohickeys";
    }
}