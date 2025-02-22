package com.app.quickchat.repository;

import com.app.quickchat.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByMobileNo(String mobileNo);
}