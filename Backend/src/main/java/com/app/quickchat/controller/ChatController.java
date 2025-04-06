package com.app.quickchat.controller;

import com.app.quickchat.model.Chat;
import com.app.quickchat.model.ChatMessage;
import com.app.quickchat.repository.ChatRepository;
import com.app.quickchat.service.ChatService;
import com.app.quickchat.model.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/fetchMessages/{chatId}")
    public ResponseDTO<Chat> fetchMessages(@PathVariable String chatId) {
        logger.info("Fetching chat details for chat ID: {}", chatId);
        Optional<Chat> opChat = chatRepository.findById(chatId);
        if (opChat.isPresent()) {
            return new ResponseDTO<>(true, "Chat retrieved successfully", opChat.get());
        }
        return new ResponseDTO<>(false, "Chat not found", null);
    }

    @PostMapping("/initiateChat/{chatId}")
    public ResponseDTO<String> initiateChat(@PathVariable String chatId) {
        logger.info("Initiating chat for chat ID: {}", chatId);
        chatService.initiateChat(chatId);
        return new ResponseDTO<>(true, "Chat initiated successfully", null);
    }

    // REST endpoint for sending a message (can still be used)
    @PostMapping("/sendMessage/{chatId}")
    public ResponseDTO<String> sendMessage(@PathVariable String chatId, @RequestBody ChatMessage chatMessage) {
        logger.info("Sending message to chat ID: {} - Message: {}", chatId, chatMessage.getMessage());
        chatService.sendMessage(chatId, chatMessage);
        // Also broadcast the message in real-time
        messagingTemplate.convertAndSend("/topic/chat/" + chatId, chatMessage);
        return new ResponseDTO<>(true, "Message sent successfully", null);
    }

    // WebSocket endpoint for receiving messages from clients
    // Clients should send to /app/sendMessage/{chatId} and this method will process it.
    @MessageMapping("/sendMessage/{chatId}")
    @SendTo({"/topic/chat/{chatId}", "/topic/global"})
    public ChatMessage processMessage(@DestinationVariable String chatId, ChatMessage chatMessage) {
        logger.info("Processing WebSocket message for chat ID: {} - Message: {}", chatId, chatMessage.getMessage());
        chatService.sendMessage(chatId, chatMessage);
        return chatMessage;
    }
}
