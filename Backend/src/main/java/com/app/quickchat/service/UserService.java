package com.app.quickchat.service;

import com.app.quickchat.model.User;
import com.app.quickchat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public String registerUser(User user){
        User dbUser = userRepository.findByMobileNo(user.getMobileNo());
        if(dbUser == null){
            userRepository.save(user);
            return "User added successfully";
        }
        return "User already exists! Please login.";
    }

    @Transactional
    public String loginUser(String mobileNo, String password){
        User user = userRepository.findByMobileNo(mobileNo);
        if(user == null){
            return "No user found with that Mobile number. Please Sign up first!";
        }
        String dbPass = user.getPassword();
        if(dbPass.equals(password)){
            return "Login successful";
        }
        return "Invalid password!";
    }
}
