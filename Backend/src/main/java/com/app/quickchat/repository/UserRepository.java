package com.app.quickchat.repository;

import com.app.quickchat.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByMobileNo(String mobileNo);

    @Query(value = "{ '_id': { $regex: '^?0', $options: 'i' } }", fields = "{ '_id': 1 }")
    List<String> findMobileNumbersByPrefix(String mobileNo);
}