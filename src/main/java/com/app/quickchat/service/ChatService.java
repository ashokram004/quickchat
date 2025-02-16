package com.app.quickchat.service;

import com.app.quickchat.model.Chat;
import com.app.quickchat.model.ChatHistory;
import com.app.quickchat.model.ChatMessage;
import com.app.quickchat.model.User;
import com.app.quickchat.repository.ChatRepository;
import com.app.quickchat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    private final MongoTemplate mongoTemplate;

    @Transactional
    public void initiateChat(String chatId) {
        // Split chatId to extract mobile numbers
        String[] mobileNos = chatId.split("\\.");
        if (mobileNos.length != 2) {
            throw new IllegalArgumentException("Invalid chatId format. Expected format: mobile1.mobile2");
        }

        String srcMobileNo = mobileNos[0];
        String targetMobileNo = mobileNos[1];

        // Define chat history entries
        ChatHistory srcChatHistory = new ChatHistory();
        srcChatHistory.setFriendMobileNo(targetMobileNo);
        srcChatHistory.setChatId(chatId);

        ChatHistory targetChatHistory = new ChatHistory();
        targetChatHistory.setFriendMobileNo(srcMobileNo);
        targetChatHistory.setChatId(chatId);

        // Update source user's chat history
        Query srcQuery = new Query(Criteria.where("mobileNo").is(srcMobileNo));
        Update srcUpdate = new Update().push("chatHistory", srcChatHistory);
        mongoTemplate.updateFirst(srcQuery, srcUpdate, User.class);

        // Update target user's chat history
        Query targetQuery = new Query(Criteria.where("mobileNo").is(targetMobileNo));
        Update targetUpdate = new Update().push("chatHistory", targetChatHistory);
        mongoTemplate.updateFirst(targetQuery, targetUpdate, User.class);

        // Create an empty chat record
        Chat chat = new Chat();
        chat.setChatId(chatId);
        chat.setChatMessages(new ArrayList<>()); // Initialize an empty list
        chatRepository.save(chat);
    }

    public void sendMessage(String chatId, ChatMessage chatMessage) {
        Query query = new Query(Criteria.where("chatId").is(chatId));
        Update update = new Update().push("chatMessages", chatMessage);

        mongoTemplate.updateFirst(query, update, Chat.class);
    }
}
