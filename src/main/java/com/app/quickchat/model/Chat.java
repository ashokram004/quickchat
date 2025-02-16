package com.app.quickchat.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "chats")
public class Chat {
    @Id
    private String chatId;         // Unique chat identifier (ourMobileNo + receiverMobileNo)
    private List<ChatMessage> chatMessages; // List of messages in this chat
}
