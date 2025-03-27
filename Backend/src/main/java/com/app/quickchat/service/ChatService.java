package com.app.quickchat.service;

import com.app.quickchat.model.Chat;
import com.app.quickchat.model.ChatHistory;
import com.app.quickchat.model.ChatMessage;
import com.app.quickchat.model.User;
import com.app.quickchat.repository.ChatRepository;
import com.app.quickchat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // Import SLF4J Logger
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

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MongoTemplate mongoTemplate;

    @Transactional
    public void initiateChat(String chatId) {
        logger.info("Initiating chat with chatId: {}", chatId);

        // Split chatId to extract mobile numbers
        String[] mobileNos = chatId.split("\\.");
        if (mobileNos.length != 2) {
            logger.error("Invalid chatId format: {}. Expected format: mobile1.mobile2", chatId);
            throw new IllegalArgumentException("Invalid chatId format. Expected format: mobile1.mobile2");
        }

        String srcMobileNo = mobileNos[0];
        String targetMobileNo = mobileNos[1];

        logger.info("Chat initiated between {} and {}", srcMobileNo, targetMobileNo);

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
        logger.info("Updated chat history for user: {}", srcMobileNo);

        // Update target user's chat history
        Query targetQuery = new Query(Criteria.where("mobileNo").is(targetMobileNo));
        Update targetUpdate = new Update().push("chatHistory", targetChatHistory);
        mongoTemplate.updateFirst(targetQuery, targetUpdate, User.class);
        logger.info("Updated chat history for user: {}", targetMobileNo);

        // Create an empty chat record
        Chat chat = new Chat();
        chat.setChatId(chatId);
        chat.setChatMessages(new ArrayList<>()); // Initialize an empty list
        chatRepository.save(chat);
        logger.info("Chat created successfully with chatId: {}", chatId);
    }

    public void sendMessage(String chatId, ChatMessage chatMessage) {
        logger.info("Sending message in chatId: {}", chatId);

        try {
            Query query = new Query(Criteria.where("chatId").is(chatId));
            Update update = new Update().push("chatMessages", chatMessage);
            mongoTemplate.updateFirst(query, update, Chat.class);

            logger.info("Message sent successfully in chatId: {} at {}", chatId, chatMessage.getTimestamp());
        } catch (Exception e) {
            logger.error("Error sending message in chatId: {}", chatId, e);
            throw e;
        }
    }
}
