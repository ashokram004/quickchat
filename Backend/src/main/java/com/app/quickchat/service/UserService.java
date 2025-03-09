package com.app.quickchat.service;

import com.app.quickchat.model.User;
import com.app.quickchat.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public String registerUser(User user){
        User dbUser = userRepository.findByMobileNo(user.getMobileNo());
        if(dbUser == null){
            user.setChatHistory(new ArrayList<>());
            user.setTheme("cyan");
            userRepository.save(user);
            return "User added successfully";
        }
        return "User already exists! Please login.";
    }

    @Transactional
    public User loginUser(String mobileNo, String password){
        User user = userRepository.findByMobileNo(mobileNo);
        if(user == null){
            return null;
        }
        String dbPass = user.getPassword();
        if(dbPass.equals(password)){
            return user;
        }
        return null;
    }

    @Transactional
    public User updateUser(String mobileNo, User user){
        User dbUser = userRepository.findByMobileNo(mobileNo);
        if(dbUser != null){
            if(user.getName() != null) {
                dbUser.setName(user.getName());
            }
            if (user.getTheme() != null) {
                dbUser.setTheme(user.getTheme());
            }
            if (user.getPassword() != null) {
                dbUser.setPassword(user.getPassword());
            }
            userRepository.save(dbUser);
            return dbUser;
        }
        return null;
    }

    @Transactional
    public List<String> searchUserMobileNosByPrefix(String mobileNo) {
        List<String> mobs = userRepository.findMobileNumbersByPrefix(mobileNo);
        return mobs.stream()
                .map(json -> {
                    try {
                        return objectMapper.readTree(json).get("_id").asText();
                    } catch (Exception e) {
                        throw new RuntimeException("Error parsing JSON: " + json, e);
                    }
                })
                .collect(Collectors.toList());
    }
}
