package com.example.chatapp.controller;

import com.example.chatapp.model.ChatMessage;
import com.example.chatapp.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController
//@RequestMapping("/api")
//public class ChatController {
//
//    private final ChatService chatService;
//
//    public ChatController(ChatService chatService) {
//        this.chatService = chatService;
//    }
//
//    @GetMapping("/getMessage")
//    public Map<String, Object> getChatData(@RequestParam(required = false) String key) {
//        Map<String, Object> data = new HashMap<>();
//        List<String> messages = chatService.getMessages();
//        System.out.println(messages);
//        if (messages != null && messages.size() > 0) {
//            String id = "admin1";
//            String message = messages.get(messages.size() - 2);
//            data.put("id", id);
//            data.put("message", message);
//        }
//        System.out.println(data + "@@@@@@");
//        return data;
//    }
//
//    @MessageMapping("/chat")
//    @SendTo("/topic/messages")
//    public String send(String message) {
//        System.out.println(message + "@@@@@@@@!!");
//        return message;
//    }
//
//    @PostMapping("/messages")
//    public List<String> sendMessage(@RequestBody ChatMessage message) {
////        String id = message.getSender();
////        String name = message.getMessage();
////        ArrayList<String> messages = new ArrayList<>();
////        messages.add(id);
////        messages.add(name);
//        List<String> messages = new ArrayList<>();
//        messages.add(message.getMessage());
//        messages.add(message.getSender());
//
//        return messages;
//    }
//
//    @PostMapping("/test")
//    public List<String> testMessage(@RequestBody ChatMessage message) {
////        List<ChatMessage> messages = new ArrayList<>();
////        messages.add(message);
////        return messages;
//        String text = message.getMessage();
//        chatService.saveMessages(text);
//        chatService.saveMessages(message.getSender());
//        return chatService.getMessages();
//    }
//}

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/messages")
    public List<ChatMessage> getMessages() {
        return chatService.getMessages();
    }
}