package com.schedule_projects.schedule_projects.controller;

import com.schedule_projects.schedule_projects.domain.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        message.setContent(message.getSender() + " : " + message.getContent());
        return message; // 메시지를 그대로 반환하여 브로드캐스트
    }
}
