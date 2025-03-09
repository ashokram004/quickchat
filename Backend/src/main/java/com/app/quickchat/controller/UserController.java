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

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{mobileNo}")
    public User getUserByMobileNo(@PathVariable String mobileNo) {
        System.out.println("Got request for fetching user details: "+mobileNo);
        return userRepository.findByMobileNo(mobileNo);
    }

    @GetMapping("/search")
    public List<String> getUserMobileNosByQuery(@RequestParam String mobileNo) {
        System.out.println("Got request for fetching user details: " + mobileNo);
        List<String> mobileNos = userService.searchUserMobileNosByPrefix(mobileNo);
        if (mobileNos.isEmpty()) {
            return null;
        }
        return mobileNos;
    }

    @PostMapping("/login")
    public User loginUser(@RequestBody User user) {
        System.out.println("Got request for login user: "+user.toString());
        return userService.loginUser(user.getMobileNo(), user.getPassword());
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        System.out.println("Got request for register user details: " + user.toString());
        return userService.registerUser(user);
    }

    @PutMapping("/update/{mobileNo}")
    public User updateUser(@PathVariable String mobileNo, @RequestBody User user) {
        System.out.println("Got request for update user details: " + user.toString());
        return userService.updateUser(mobileNo, user);
    }
}
