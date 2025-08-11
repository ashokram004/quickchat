package com.app.quickchat.service.dynamo;

import com.app.quickchat.model.dynamo.ChatDynamo;
import com.app.quickchat.model.dynamo.ChatHistoryDynamo;
import com.app.quickchat.model.dynamo.ChatMessageDynamo;
import com.app.quickchat.model.dynamo.UserDynamo;
import com.app.quickchat.repository.dynamo.ChatDynamoDao;
import com.app.quickchat.repository.dynamo.UserDynamoDao;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceDynamo {

    private static final Logger logger = LoggerFactory.getLogger(ChatServiceDynamo.class);

    private final UserDynamoDao userDynamoDao;
    private final ChatDynamoDao chatDynamoDao;

    @Transactional
    public void initiateChat(String chatId) {
        logger.info("Initiating chat with chatId: {}", chatId);

        String[] mobileNos = chatId.split("\\.");
        if (mobileNos.length != 2) {
            logger.error("Invalid chatId format: {}. Expected format: mobile1.mobile2", chatId);
            throw new IllegalArgumentException("Invalid chatId format. Expected format: mobile1.mobile2");
        }

        String srcMobileNo = mobileNos[0];
        String targetMobileNo = mobileNos[1];

        logger.info("Chat initiated between {} and {}", srcMobileNo, targetMobileNo);

        // Define chat history entries
        ChatHistoryDynamo srcChatHistory = new ChatHistoryDynamo();
        srcChatHistory.setFriendMobileNo(targetMobileNo);
        srcChatHistory.setChatId(chatId);

        ChatHistoryDynamo targetChatHistory = new ChatHistoryDynamo();
        targetChatHistory.setFriendMobileNo(srcMobileNo);
        targetChatHistory.setChatId(chatId);

        // Update source user's chat history
        UserDynamo srcUser = userDynamoDao.findByMobileNo(srcMobileNo);
        if (srcUser != null) {
            List<ChatHistoryDynamo> chatHistory = srcUser.getChatHistory() != null ? srcUser.getChatHistory() : new ArrayList<>();
            chatHistory.add(srcChatHistory);
            srcUser.setChatHistory(chatHistory);
            userDynamoDao.save(srcUser);
            logger.info("Updated chat history for user: {}", srcMobileNo);
        }

        // Update target user's chat history
        UserDynamo targetUser = userDynamoDao.findByMobileNo(targetMobileNo);
        if (targetUser != null) {
            List<ChatHistoryDynamo> chatHistory = targetUser.getChatHistory() != null ? targetUser.getChatHistory() : new ArrayList<>();
            chatHistory.add(targetChatHistory);
            targetUser.setChatHistory(chatHistory);
            userDynamoDao.save(targetUser);
            logger.info("Updated chat history for user: {}", targetMobileNo);
        }

        // Create an empty chat record
        ChatDynamo chat = new ChatDynamo();
        chat.setChatId(chatId);
        chat.setChatMessages(new ArrayList<>());
        chatDynamoDao.save(chat);
        logger.info("Chat created successfully with chatId: {}", chatId);
    }

    public void sendMessage(String chatId, ChatMessageDynamo chatMessage) {
        logger.info("Sending message in chatId: {}", chatId);

        try {
            ChatDynamo chat = chatDynamoDao.findById(chatId);
            if (chat != null) {
                List<ChatMessageDynamo> messages = chat.getChatMessages() != null ? chat.getChatMessages() : new ArrayList<>();
                messages.add(chatMessage);
                chat.setChatMessages(messages);
                chatDynamoDao.save(chat);
            }
            logger.info("Message sent successfully in chatId: {} at {}", chatId, chatMessage.getTimestamp());
        } catch (Exception e) {
            logger.error("Error sending message in chatId: {}", chatId, e);
            throw e;
        }
    }

    public ChatDynamo getChatById(String chatId) {
        return chatDynamoDao.findById(chatId);
    }
}