package com.app.quickchat.repository;

import com.app.quickchat.model.Chat;
import com.app.quickchat.model.ChatMessage;
import com.app.quickchat.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {

}