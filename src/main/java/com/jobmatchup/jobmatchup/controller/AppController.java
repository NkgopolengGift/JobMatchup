package com.jobmatchup.jobmatchup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jobmatchup.jobmatchup.entities.user.User;
import com.jobmatchup.jobmatchup.entities.user.UserRepository;
import com.jobmatchup.jobmatchup.service.UserService;

@Controller
public class AppController {

    @Autowired
    private UserRepository repo;
    @Autowired
    private UserService userService;

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

    //################-sign up processing-################
    @PostMapping("/process_register")
    public String processRegister(User user, RedirectAttributes redirectAttributes) {
        User existingUser = repo.findByEmail(user.getEmail());
        if(existingUser != null){
            redirectAttributes.addFlashAttribute("message", "User exists. Please login");
            return "redirect:/sign_in";
        }

        repo.save(user);
        redirectAttributes.addFlashAttribute("message", "Your account has been created successfully. Please login.");
        return "redirect:/sign_in";
    }
    
    //################-login processing-################
    @PostMapping("/process_login")
    public String processLogin(@RequestParam String email, @RequestParam String password, Model model) {
        
        User user = userService.authenticate(email, password);
        if (user != null) {

            model.addAttribute("firstName", user.getFirstName());
            return "home";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "sign_in";
        }
    }

    //################-Validate reset-################
    @PostMapping("/validate_rest")
     public String  resetPassword(@RequestParam String email, @RequestParam String newPassword, Model model, RedirectAttributes redirectAttributes) {
        User user = repo.findByEmail(email);
        if (user != null) {
    
            if(!user.getPassword().equals(newPassword)){
                user.setPassword(newPassword);
                repo.save(user);
                redirectAttributes.addFlashAttribute("message", "Password has been successfully reset. Please login.");
                return "redirect:/sign_in";
            }
            else{
                model.addAttribute("error", "You can't use the same password as the old one. Choose another password.");
                return "forgot";
            }

        } else {
            model.addAttribute("error", "Email not found. Please try again.");
            return "forgot";
        }  
    }

    //################-Log out-################
    @GetMapping("/logout")
    public String logOut() {
        return "index";
    }
}

