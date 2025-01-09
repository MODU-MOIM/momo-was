package com.example.momowas.chat.controller;


import com.example.momowas.chat.domain.Chat;
import com.example.momowas.chat.domain.ChatRoom;
import com.example.momowas.chat.dto.ChatDto;
import com.example.momowas.chat.dto.ChatRoomDto;
import com.example.momowas.chat.dto.ChatRoomReqDto;
import com.example.momowas.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;

    @GetMapping("/room/{roomId}/history")
    public List<ChatDto> chatHistory(@PathVariable Long roomId) {
        return chatService.findAllChatByRoomId(roomId);
    }
    @GetMapping("/room/{roomId}")
    public ChatRoomDto joinRoom(@PathVariable Long roomId) {
        return chatService.findRoomById(roomId);
    }

    @PostMapping("/room")
    public ChatRoomDto roomList(@RequestBody ChatRoomReqDto chatRoomReqDto) {
        return chatService.createRoom(chatRoomReqDto.getName());
    }

    @GetMapping("/rooms")
    public List<ChatRoomDto> roomList() {
        return chatService.findAllRoom();
    }

}