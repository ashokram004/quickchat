package com.app.quickchat.service;

import com.app.quickchat.model.User;
import com.app.quickchat.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // Import SLF4J Logger
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    UserRepository userRepository;

    @Transactional
    public String registerUser(User user) {
        logger.info("Registering user with mobile number: {}", user.getMobileNo());

        User dbUser = userRepository.findByMobileNo(user.getMobileNo());
        if (dbUser == null) {
            user.setChatHistory(new ArrayList<>());
            user.setTheme("cyan");
            userRepository.save(user);
            logger.info("User registered successfully: {}", user.getMobileNo());
            return "User registered successfully";
        }

        logger.warn("User already exists with mobile number: {}", user.getMobileNo());
        return "User already exists! Please login.";
    }

    @Transactional
    public User loginUser(String mobileNo, String password) {
        logger.info("Attempting login for mobile number: {}", mobileNo);

        User user = userRepository.findByMobileNo(mobileNo);
        if (user == null) {
            logger.warn("Login failed: No user found with mobile number {}", mobileNo);
            return null;
        }

        if (user.getPassword().equals(password)) {
            logger.info("Login successful for user: {}", mobileNo);
            return user;
        }

        logger.warn("Login failed: Incorrect password for user {}", mobileNo);
        return null;
    }

    @Transactional
    public User getUser(String mobileNo) {
        logger.info("Searching for user with mobile number: {}", mobileNo);

        User user = userRepository.findByMobileNo(mobileNo);
        if (user == null) {
            logger.warn("No user found with mobile number {}", mobileNo);
            return null;
        }
        logger.info("Search successful for user: {}", mobileNo);
        return user;
    }

    @Transactional
    public User updateUser(String mobileNo, User user) {
        logger.info("Updating user with mobile number: {}", mobileNo);

        User dbUser = userRepository.findByMobileNo(mobileNo);
        if (dbUser != null) {
            if (user.getName() != null) {
                logger.info("Updating name for user: {}", mobileNo);
                dbUser.setName(user.getName());
            }
            if (user.getTheme() != null) {
                logger.info("Updating theme for user: {}", mobileNo);
                dbUser.setTheme(user.getTheme());
            }
            if (user.getPassword() != null) {
                logger.info("Updating password for user: {}", mobileNo);
                dbUser.setPassword(user.getPassword());
            }

            userRepository.save(dbUser);
            logger.info("User updated successfully: {}", mobileNo);
            return dbUser;
        }

        logger.warn("User update failed: No user found with mobile number {}", mobileNo);
        return null;
    }

    @Transactional
    public List<String> searchUserMobileNosByPrefix(String mobileNo) {
        logger.info("Searching users with mobile number prefix: {}", mobileNo);

        List<String> mobs = userRepository.findMobileNumbersByPrefix(mobileNo);
        try {
            List<String> results = mobs.stream()
                    .map(json -> {
                        try {
                            return objectMapper.readTree(json).get("_id").asText();
                        } catch (Exception e) {
                            logger.error("Error parsing JSON for user search: {}", json, e);
                            throw new RuntimeException("Error parsing JSON: " + json, e);
                        }
                    })
                    .collect(Collectors.toList());

            logger.info("Search completed. Found {} results for prefix: {}", results.size(), mobileNo);
            return results;
        } catch (Exception e) {
            logger.error("Unexpected error while searching users by prefix: {}", mobileNo, e);
            throw e;
        }
    }
}
