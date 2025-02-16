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
    public String addUser(User user){
        userRepository.save(user);
        return "User added successfully";
    }
}
