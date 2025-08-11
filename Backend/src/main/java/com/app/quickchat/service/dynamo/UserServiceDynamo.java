package com.app.quickchat.service.dynamo;

import com.app.quickchat.model.dynamo.UserDynamo;
import com.app.quickchat.repository.dynamo.UserDynamoDao;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceDynamo {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceDynamo.class);

    private final UserDynamoDao userDynamoDao;

    @Transactional
    public String registerUser(UserDynamo user) {
        logger.info("Registering user with mobile number: {}", user.getMobileNo());

        UserDynamo dbUser = userDynamoDao.findByMobileNo(user.getMobileNo());
        if (dbUser == null) {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);
            user.setChatHistory(new ArrayList<>());
            user.setTheme("cyan");
            userDynamoDao.save(user);
            logger.info("User registered successfully: {}", user.getMobileNo());
            return "User registered successfully";
        }

        logger.warn("User already exists with mobile number: {}", user.getMobileNo());
        return "User already exists! Please login.";
    }

    @Transactional
    public UserDynamo loginUser(String mobileNo, String password) {
        logger.info("Attempting login for mobile number: {}", mobileNo);

        UserDynamo user = userDynamoDao.findByMobileNo(mobileNo);
        if (user == null) {
            logger.warn("Login failed: No user found with mobile number {}", mobileNo);
            return null;
        }

        if (BCrypt.checkpw(password, user.getPassword())) {
            logger.info("Login successful for user: {}", mobileNo);
            return user;
        }

        logger.warn("Login failed: Incorrect password for user {}", mobileNo);
        return null;
    }

    @Transactional
    public UserDynamo getUser(String mobileNo) {
        logger.info("Searching for user with mobile number: {}", mobileNo);

        UserDynamo user = userDynamoDao.findByMobileNo(mobileNo);
        if (user == null) {
            logger.warn("No user found with mobile number {}", mobileNo);
            return null;
        }
        logger.info("Search successful for user: {}", mobileNo);
        return user;
    }

    @Transactional
    public List<UserDynamo> getAllUsers() {
        logger.info("Fetching all users (DynamoDB)");

        List<UserDynamo> users = userDynamoDao.findAll();
        if (users == null || users.isEmpty()) {
            logger.warn("No users found in the database");
            return null;
        }
        logger.info("Found {} users in the database", users.size());
        return users;
    }

    @Transactional
    public UserDynamo updateUser(String mobileNo, UserDynamo user) {
        logger.info("Updating user with mobile number: {}", mobileNo);

        UserDynamo dbUser = userDynamoDao.findByMobileNo(mobileNo);
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
            userDynamoDao.save(dbUser);
            logger.info("User updated successfully: {}", mobileNo);
            return dbUser;
        }

        logger.warn("User update failed: No user found with mobile number {}", mobileNo);
        return null;
    }

    @Transactional
    public List<String> searchUserMobileNosByPrefix(String mobileNo) {
        logger.info("Searching users with mobile number prefix: {}", mobileNo);

        List<String> results = userDynamoDao.findMobileNumbersByPrefix(mobileNo);
        logger.info("Search completed. Found {} results for prefix: {}", results.size(), mobileNo);
        return results;
    }
}