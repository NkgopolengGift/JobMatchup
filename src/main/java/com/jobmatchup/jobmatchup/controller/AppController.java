package com.jobmatchup.jobmatchup.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jobmatchup.jobmatchup.entities.chat.Post;
import com.jobmatchup.jobmatchup.entities.chat.PostRepository;
import com.jobmatchup.jobmatchup.entities.user.User;
import com.jobmatchup.jobmatchup.entities.user.UserRepository;
import com.jobmatchup.jobmatchup.service.EmailService;
import com.jobmatchup.jobmatchup.service.UserService;

@Controller
@SessionAttributes("loggedInUser")
public class AppController {

    @Autowired
    private UserRepository repo;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

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

    //################-home page-################
    @GetMapping("/home")
    public String home(Model model, @SessionAttribute("loggedInUser") User loggedInUser) {

         List<Post> posts = postRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        // Create a list to hold post details including author's first name
        List<Map<String, Object>> postDetails = new ArrayList<>();

        for (Post post : posts) {
            Map<String, Object> details = new HashMap<>();
            details.put("id", post.getId());
            details.put("jobTitle", post.getJobTitle());
            details.put("content", post.getContent());
            details.put("createdAt", post.getCreatedAt());

            // Retrieve author's first name
            User author = post.getAuthor();
            details.put("authorFirstName", author.getFirstName());

            postDetails.add(details);
        }

        model.addAttribute("posts", postDetails);
        model.addAttribute("firstName", loggedInUser.getFirstName());
        return "home";
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
    public String processLogin(@RequestParam String email, @RequestParam String password, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.authenticate(email, password);
        if (user != null) {
			model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("loggedInUser", user);
            return "redirect:/home"; // Ensure we redirect to home after login
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/sign_in";
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
    //################-Post-################
    @PostMapping("/post")
    public String postJobs(@RequestParam String jobTitle, @RequestParam String postContent, @SessionAttribute("loggedInUser") 
         User loggedInUser, RedirectAttributes redirectAttributes)
        {
            if (loggedInUser == null) {
                redirectAttributes.addFlashAttribute("message", "You must be logged in to post.");
                return "redirect:/sign_in";
            }
    
            Post newPost = new Post(loggedInUser, jobTitle, postContent, LocalDateTime.now());
            postRepo.save(newPost);
    
            redirectAttributes.addFlashAttribute("message", "Post created successfully.");
            return "redirect:/home";
        }

    //################-Apply-################
    @PostMapping("/apply")
    public String applyForJob(@RequestParam Long postId, @SessionAttribute("loggedInUser") User loggedInUser, RedirectAttributes redirectAttributes) {
        Post post = postRepo.findById(postId).orElse(null);
        if (post == null) {
            redirectAttributes.addFlashAttribute("message", "Post not found.");
            return "redirect:/home";
        }

        User postAuthor = post.getAuthor();
        String subject = "Job Application for " + post.getJobTitle();
        String body = "Dear " + postAuthor.getFirstName() + ",\n\n" +
                      "You have received a new job application for your post \"" + post.getJobTitle() + "\" from " + loggedInUser.getFirstName() + " " + loggedInUser.getSurname() + ".\n\n" +
                      "Applicant Details:\n" +
                      "Name: " + loggedInUser.getFirstName() + " " + loggedInUser.getSurname() + "\n" +
                      "Email: " + loggedInUser.getEmail() + "\n\n" +
                      "Regards,\nJobMatchup Team";

        emailService.sendEmail(postAuthor.getEmail(), subject, body);

        redirectAttributes.addFlashAttribute("message", "Your application has been sent successfully.");
        return "redirect:/home";
    }
}

