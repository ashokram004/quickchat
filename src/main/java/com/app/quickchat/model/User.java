package com.app.quickchat.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String mobileNo; // Unique identifier
    private String name;
    private String password;
    private List<ChatHistory> chatHistory; // Stores chat history with other users
}
