package com.app.quickchat.model;

import lombok.Data;

@Data
public class ChatHistory {
    private String friendMobileNo; // The other person's number
    private String chatId;           // Unique chat ID (our mobile + receiver mobile)
}
