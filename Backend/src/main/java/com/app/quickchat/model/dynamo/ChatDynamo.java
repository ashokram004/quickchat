package com.app.quickchat.model.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import java.util.List;

@DynamoDbBean
public class ChatDynamo {
    private String chatId;
    private List<ChatMessageDynamo> chatMessages;

    @DynamoDbPartitionKey
    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }

    public List<ChatMessageDynamo> getChatMessages() { return chatMessages; }
    public void setChatMessages(List<ChatMessageDynamo> chatMessages) { this.chatMessages = chatMessages; }
}