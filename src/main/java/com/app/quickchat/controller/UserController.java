package com.app.quickchat.controller;

import com.app.quickchat.model.User;
import com.app.quickchat.repository.UserRepository;
import com.app.quickchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public String createUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{mobileNo}")
    public User getUserByMobileNo(@PathVariable String mobileNo) {
        return userRepository.findByMobileNo(mobileNo);
    }
}
