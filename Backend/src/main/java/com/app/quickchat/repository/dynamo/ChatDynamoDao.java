package com.app.quickchat.repository.dynamo;

import com.app.quickchat.model.dynamo.ChatDynamo;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ChatDynamoDao {
    private final DynamoDbTable<ChatDynamo> chatTable;

    @Autowired
    public ChatDynamoDao(DynamoDbEnhancedClient enhancedClient) {
        this.chatTable = enhancedClient.table("chats", TableSchema.fromBean(ChatDynamo.class));
    }

    public void save(ChatDynamo chat) {
        chatTable.putItem(chat);
    }

    public ChatDynamo findById(String chatId) {
        return chatTable.getItem(Key.builder().partitionValue(chatId).build());
    }

    public List<ChatDynamo> findAll() {
        List<ChatDynamo> chats = new ArrayList<>();
        chatTable.scan().items().forEach(chats::add);
        return chats;
    }
}