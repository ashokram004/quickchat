package com.app.quickchat.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private String chatId;
    private String sender;   // mobileNo of sender
    private String message;      // Actual text message
    private LocalDateTime timestamp; // Time when the message was sent
}
