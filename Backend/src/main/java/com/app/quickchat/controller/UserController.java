package com.app.quickchat.controller;

import com.app.quickchat.config.JWTConfig;
import com.app.quickchat.model.User;
import com.app.quickchat.repository.UserRepository;
import com.app.quickchat.service.UserService;
import com.app.quickchat.model.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTConfig jwtConfig;

    @GetMapping
    public ResponseDTO<List<User>> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userRepository.findAll();
        return new ResponseDTO<>(true, "Users retrieved successfully", users);
    }

    @GetMapping("/{mobileNo}")
    public ResponseDTO<User> getUserByMobileNo(@PathVariable String mobileNo) {
        logger.info("Fetching user details for mobile number: {}", mobileNo);
        User user = userRepository.findByMobileNo(mobileNo);
        if (user == null) {
            return new ResponseDTO<>(false, "User not found", null);
        }
        return new ResponseDTO<>(true, "User found", user);
    }

    @GetMapping("/search")
    public ResponseDTO<List<String>> getUserMobileNosByQuery(@RequestParam String mobileNo) {
        logger.info("Searching users with mobile prefix: {}", mobileNo);
        List<String> mobileNos = userService.searchUserMobileNosByPrefix(mobileNo);
        if (mobileNos.isEmpty()) {
            return new ResponseDTO<>(false, "No users found", null);
        }
        return new ResponseDTO<>(true, "Users found", mobileNos);
    }

    @PostMapping("/login")
    public ResponseDTO<Map<String, Object>> loginUser(@RequestBody User user) {
        logger.info("Login request for mobile number: {}", user.getMobileNo());
        User loggedInUser = userService.loginUser(user.getMobileNo(), user.getPassword());
        if (loggedInUser == null) {
            return new ResponseDTO<>(false, "Invalid credentials", null);
        }
        // Generate JWT token
        String token = jwtConfig.generateToken(loggedInUser.getMobileNo());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("token", token);
        responseData.put("user", loggedInUser);

        return new ResponseDTO<>(true, "Login successful", responseData);
    }

    @PostMapping("/register")
    public ResponseDTO<String> registerUser(@RequestBody User user) {
        logger.info("Register request for mobile number: {}", user.getMobileNo());
        String result = userService.registerUser(user);
        return new ResponseDTO<>(true, result, null);
    }

    @PutMapping("/update/{mobileNo}")
    public ResponseDTO<User> updateUser(@PathVariable String mobileNo, @RequestBody User user) {
        logger.info("Updating user details for mobile number: {}", mobileNo);
        User updatedUser = userService.updateUser(mobileNo, user);
        if (updatedUser == null) {
            return new ResponseDTO<>(false, "User update failed", null);
        }
        return new ResponseDTO<>(true, "User updated successfully", updatedUser);
    }
}
