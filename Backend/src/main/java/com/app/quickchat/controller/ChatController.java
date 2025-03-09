package com.app.quickchat.controller;

import com.app.quickchat.model.Chat;
import com.app.quickchat.model.ChatMessage;
import com.app.quickchat.repository.ChatRepository;
import com.app.quickchat.repository.UserRepository;
import com.app.quickchat.service.ChatService;
import com.app.quickchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @GetMapping("/fetchMessages/{chatId}")
    public Chat fetchMessages(@PathVariable String chatId) {
        System.out.println("Got request for fetch chat details: " + chatId);
        Optional<Chat> opChat = chatRepository.findById(chatId);
        return opChat.orElse(null);
    }

    @PostMapping("/initiateChat/{chatId}")
    public String initiateChat(@PathVariable String chatId) {
        System.out.println("Got request for initiate chat: " + chatId);
        chatService.initiateChat(chatId);
        return "Chat details created";
    }

    @PostMapping("/sendMessage/{chatId}")
    public String sendMessage(@PathVariable String chatId, @RequestBody ChatMessage chatMessage) {
        System.out.println("Got request for sending message: " + chatMessage.toString());
        chatService.sendMessage(chatId, chatMessage);
        return "Message sent";
    }
}
