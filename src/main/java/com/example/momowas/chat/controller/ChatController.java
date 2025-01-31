package com.example.momowas.chat.controller;

import com.example.momowas.chat.dto.ChatDto;
import com.example.momowas.chat.dto.ChatReqDto;
import com.example.momowas.chat.service.ChatService;
import com.example.momowas.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final JwtUtil jwtUtil;

    @MessageMapping("/room/{roomId}/message")
    @SendTo("/room/{roomId}")
    public ChatDto sendChatMessage( @DestinationVariable Long roomId, @RequestBody ChatReqDto chatReqDto, @Header("token") String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        return chatService.createChat(roomId, chatReqDto, userId);
    }

    @MessageMapping("/room/{roomId}/enter")
    @SendTo("/room/{roomId}")
    public ChatDto enterChatRoomMessage( @DestinationVariable Long roomId, @Header("token") String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        return chatService.enterChatRoomChat(roomId, userId);
    }

}