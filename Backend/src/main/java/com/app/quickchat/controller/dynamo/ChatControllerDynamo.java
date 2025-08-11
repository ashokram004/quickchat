package com.app.quickchat.controller.dynamo;

import com.app.quickchat.model.dynamo.ChatDynamo;
import com.app.quickchat.model.dynamo.ChatMessageDynamo;
import com.app.quickchat.model.ResponseDTO;
import com.app.quickchat.service.dynamo.ChatServiceDynamo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dynamo/chats")
public class ChatControllerDynamo {

    private static final Logger logger = LoggerFactory.getLogger(ChatControllerDynamo.class);

    @Autowired
    private ChatServiceDynamo chatServiceDynamo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/fetchMessages/{chatId}")
    public ResponseDTO<ChatDynamo> fetchMessages(@PathVariable String chatId) {
        logger.info("Fetching chat details for chat ID: {}", chatId);
        ChatDynamo chat = chatServiceDynamo.getChatById(chatId);
        if (chat != null) {
            return new ResponseDTO<>(true, "Chat retrieved successfully", chat);
        }
        return new ResponseDTO<>(false, "Chat not found", null);
    }

    @PostMapping("/initiateChat/{chatId}")
    public ResponseDTO<String> initiateChat(@PathVariable String chatId) {
        logger.info("Initiating chat for chat ID: {}", chatId);
        chatServiceDynamo.initiateChat(chatId);
        return new ResponseDTO<>(true, "Chat initiated successfully", null);
    }

    @PostMapping("/dynamo/sendMessage/{chatId}")
    public ResponseDTO<String> sendMessage(@PathVariable String chatId, @RequestBody ChatMessageDynamo chatMessage) {
        logger.info("Sending message to chat ID: {} - Message: {}", chatId, chatMessage.getMessage());
        chatServiceDynamo.sendMessage(chatId, chatMessage);
        messagingTemplate.convertAndSend("/topic/chat/" + chatId, chatMessage);
        return new ResponseDTO<>(true, "Message sent successfully", null);
    }

    @MessageMapping("/dynamo/sendMessage/{chatId}")
    @SendTo({"/topic/chat/{chatId}", "/topic/global"})
    public ChatMessageDynamo processMessage(@DestinationVariable String chatId, ChatMessageDynamo chatMessage) {
        logger.info("Processing WebSocket message for chat ID: {} - Message: {}", chatId, chatMessage.getMessage());
        chatServiceDynamo.sendMessage(chatId, chatMessage);
        return chatMessage;
    }
}