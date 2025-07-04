//package com.example.chatapp.model;
//
//public class ChatMessage {
//    private String sender;
//    private String message;
//
//    // Getters and Setters
//    public String getSender() {
//        return sender;
//    }
//    public void setSender(String sender) {
//        this.sender = sender;
//    }
//    public String getMessage() {
//        return message;
//    }
//    public void setMessage(String message) {
//        this.message = message;
//    }
//    @Override
//    public String toString() {
//        return message;
//    }
//}

package com.example.chatapp.model;

import lombok.Data;

@Data
public class ChatMessage {
    private String email;
    private String password;
//    private String sender;
//    private String message;
//
//    // ✅ 기본 생성자 추가
//    public ChatMessage() {
//    }
//
//    // Getters and Setters
//    public String getSender() {
//        return sender;
//    }
//    public void setSender(String sender) {
//        this.sender = sender;
//    }
//    public String getMessage() {
//        return message;
//    }
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    @Override
//    public String toString() {
//        return message;
//    }
}