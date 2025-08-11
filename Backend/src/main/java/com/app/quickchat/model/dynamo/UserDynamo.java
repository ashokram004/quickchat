package com.app.quickchat.model.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import java.util.List;

@DynamoDbBean
public class UserDynamo {
    private String mobileNo;
    private String name;
    private String password;
    private String theme;
    private List<ChatHistoryDynamo> chatHistory;

    @DynamoDbPartitionKey
    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public List<ChatHistoryDynamo> getChatHistory() { return chatHistory; }
    public void setChatHistory(List<ChatHistoryDynamo> chatHistory) { this.chatHistory = chatHistory; }
}