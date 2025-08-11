package com.app.quickchat.model.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
public class ChatHistoryDynamo {
    private String friendMobileNo;
    private String chatId;

    public String getFriendMobileNo() { return friendMobileNo; }
    public void setFriendMobileNo(String friendMobileNo) { this.friendMobileNo = friendMobileNo; }

    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }
}