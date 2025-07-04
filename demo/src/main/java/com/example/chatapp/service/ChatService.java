package com.example.chatapp.service;

import com.example.chatapp.model.ChatMessage;
import com.example.chatapp.repository.ChatMapper;
import org.springframework.stereotype.Service;

import java.util.List;

//package com.example.chatapp.service;
//
//import com.example.chatapp.model.ChatMessage;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class ChatService {
//    private final List<String> messages = new ArrayList<>();
//
//    public void saveMessages(String message) {
//        messages.add(message);
//        String[] text = {message};
//    }
//
//    public List<String> getMessages() {
//
//        return messages;
//    }
//}
@Service
public class ChatService {

    private final ChatMapper chatMapper;

    public ChatService(ChatMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    public List<ChatMessage> getMessages() {
        return chatMapper.getAllMessages();
    }
}