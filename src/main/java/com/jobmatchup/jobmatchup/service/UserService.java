package com.jobmatchup.jobmatchup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobmatchup.jobmatchup.entities.user.Status;
import com.jobmatchup.jobmatchup.entities.user.User;
import com.jobmatchup.jobmatchup.entities.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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

    public void saveUser(User user){
        user.setStatus(Status.ONLINE);
        repo.save(user);
    }

    public void disconnect(User user){
        var storedUser  = repo.findByEmail(user.getEmail());
                    
        if(storedUser != null){
            storedUser.setStatus(Status.OFFLINE);
            repo.save(storedUser);
        }
    }
    public List<User> findConnectedUsers(){
        return repo.findAllByStatus(Status.ONLINE);
    }
}
