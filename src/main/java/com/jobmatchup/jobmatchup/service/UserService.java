package com.jobmatchup.jobmatchup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobmatchup.jobmatchup.entities.user.User;
import com.jobmatchup.jobmatchup.entities.user.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;
    
    public User authenticate(String email, String password){
        
        User user = repo.findByEmail(email);
        if(user != null && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }
}
