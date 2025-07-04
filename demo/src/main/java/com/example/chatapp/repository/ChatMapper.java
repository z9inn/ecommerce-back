package com.example.chatapp.repository;

import com.example.chatapp.model.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ChatMapper {
    List<ChatMessage> getAllMessages();
}