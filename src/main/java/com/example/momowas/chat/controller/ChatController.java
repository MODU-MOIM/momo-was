package com.example.momowas.chat.controller;

import com.example.momowas.chat.domain.Chat;
import com.example.momowas.chat.dto.ChatDto;
import com.example.momowas.chat.dto.ChatReqDto;
import com.example.momowas.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/{roomId}")
    @SendTo("/room/{roomId}")
    public ChatDto test(@DestinationVariable Long roomId, @RequestBody  ChatReqDto chatReqDto) {
        return chatService.createChat(roomId, chatReqDto);
    }
}