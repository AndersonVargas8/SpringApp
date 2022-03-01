package com.app.springapp.controller;

import com.app.springapp.entity.User;
import com.app.springapp.repository.RoleRepository;
import com.app.springapp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController{

    @Autowired
    UserService serUser;

    @Autowired
    RoleRepository repRole;

    @GetMapping({"/","/login"})
    public String index(){
        return "index";
    }

    @GetMapping("/userForm")
    public String userForm(Model model){
        model.addAttribute("userForm",new User());
        model.addAttribute("userList", serUser.getAllUsers());
        model.addAttribute("roles",repRole.findAll());
        model.addAttribute("listTab","active");
        return "user-form/user-view";
    }
}