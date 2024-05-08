package com.jobmatchup.jobmatchup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jobmatchup.jobmatchup.user.User;
import com.jobmatchup.jobmatchup.user.UserRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class AppController {

    @Autowired
    private UserRepository repo;
    //Templates
    @GetMapping("/")
    public String viewHomePage() {
        return "index";
    }
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user",new User());
        return "sign_up";
    }
    @GetMapping("/sign_in")
    public String signIn() {
        return "sign_in";
    }
    @GetMapping("/forgot")
    public String forgotPassword() {
        return "forgot";
    }
    

    //Processing
    @PostMapping("/process_register")
    public String processRegister(User user) {
        repo.save(user);
        return "register_success";
    }
    

  
}
