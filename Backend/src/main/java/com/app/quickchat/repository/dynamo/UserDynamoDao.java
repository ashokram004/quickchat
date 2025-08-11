package com.app.quickchat.repository.dynamo;

import com.app.quickchat.model.dynamo.UserDynamo;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDynamoDao {
    private final DynamoDbTable<UserDynamo> userTable;

    @Autowired
    public UserDynamoDao(DynamoDbEnhancedClient enhancedClient) {
        this.userTable = enhancedClient.table("users", TableSchema.fromBean(UserDynamo.class));
    }

    public void save(UserDynamo user) {
        userTable.putItem(user);
    }

    public UserDynamo findByMobileNo(String mobileNo) {
        return userTable.getItem(Key.builder().partitionValue(mobileNo).build());
    }

    public List<UserDynamo> findAll() {
        List<UserDynamo> users = new ArrayList<>();
        userTable.scan().items().forEach(users::add);
        return users;
    }

    // Custom: Find mobile numbers by prefix
    public List<String> findMobileNumbersByPrefix(String prefix) {
        List<String> result = new ArrayList<>();
        userTable.scan().items().forEach(user -> {
            if (user.getMobileNo() != null && user.getMobileNo().startsWith(prefix)) {
                result.add(user.getMobileNo());
            }
        });
        return result;
    }
}