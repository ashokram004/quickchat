package com.app.quickchat.controller.dynamo;

import com.app.quickchat.config.JWTConfig;
import com.app.quickchat.model.dynamo.UserDynamo;
import com.app.quickchat.model.ResponseDTO;
import com.app.quickchat.repository.dynamo.UserDynamoDao;
import com.app.quickchat.service.dynamo.UserServiceDynamo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dynamo/users")
public class UserControllerDynamo {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerDynamo.class);

    @Autowired
    private UserServiceDynamo userServiceDynamo;

    @Autowired
    private JWTConfig jwtConfig;

    @GetMapping
    public ResponseDTO<List<UserDynamo>> getAllUsers() {
        logger.info("Fetching all users (DynamoDB)");
        List<UserDynamo> users = userServiceDynamo.getAllUsers();
        if (users == null || users.isEmpty()) {
            return new ResponseDTO<>(false, "No users found", null);
        }
        return new ResponseDTO<>(true, "Users retrieved successfully", users);
    }

    @GetMapping("/{mobileNo}")
    public ResponseDTO<UserDynamo> getUserByMobileNo(@PathVariable String mobileNo) {
        logger.info("Fetching user details for mobile number: {} (DynamoDB)", mobileNo);
        UserDynamo user = userServiceDynamo.getUser(mobileNo);
        if (user == null) {
            return new ResponseDTO<>(false, "User not found", null);
        }
        return new ResponseDTO<>(true, "User found", user);
    }

    @GetMapping("/search")
    public ResponseDTO<List<String>> getUserMobileNosByQuery(@RequestParam String mobileNo) {
        logger.info("Searching users with mobile prefix: {} (DynamoDB)", mobileNo);
        List<String> mobileNos = userServiceDynamo.searchUserMobileNosByPrefix(mobileNo);
        if (mobileNos.isEmpty()) {
            return new ResponseDTO<>(false, "No users found", null);
        }
        return new ResponseDTO<>(true, "Users found", mobileNos);
    }

    @PostMapping("/login")
    public ResponseDTO<Map<String, Object>> loginUser(@RequestBody UserDynamo user) {
        logger.info("Login request for mobile number: {} (DynamoDB)", user.getMobileNo());
        UserDynamo loggedInUser = userServiceDynamo.loginUser(user.getMobileNo(), user.getPassword());
        if (loggedInUser == null) {
            return new ResponseDTO<>(false, "Invalid credentials", null);
        }
        String token = jwtConfig.generateToken(loggedInUser.getMobileNo());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("token", token);
        responseData.put("user", loggedInUser);

        return new ResponseDTO<>(true, "Login successful", responseData);
    }

    @PostMapping("/register")
    public ResponseDTO<String> registerUser(@RequestBody UserDynamo user) {
        logger.info("Register request for mobile number: {} (DynamoDB)", user.getMobileNo());
        String result = userServiceDynamo.registerUser(user);
        return new ResponseDTO<>(true, result, null);
    }

    @PutMapping("/update/{mobileNo}")
    public ResponseDTO<UserDynamo> updateUser(@PathVariable String mobileNo, @RequestBody UserDynamo user) {
        logger.info("Updating user details for mobile number: {} (DynamoDB)", mobileNo);
        UserDynamo updatedUser = userServiceDynamo.updateUser(mobileNo, user);
        if (updatedUser == null) {
            return new ResponseDTO<>(false, "User update failed", null);
        }
        return new ResponseDTO<>(true, "User updated successfully", updatedUser);
    }
}