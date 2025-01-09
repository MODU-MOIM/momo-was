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


//    @GetMapping("/{roomId}")
//    public String joinRoom(@PathVariable(required = false) Long roomId, Model model) {
//        List<ChatDto> chatList = chatService.findAllChatByRoomId(roomId);
//
//        model.addAttribute("roomId", roomId);
//        model.addAttribute("chatList", chatList);
//        return "room";
//    }
    @PostMapping("/room")
    public ChatRoomDto roomList(@RequestBody ChatRoomReqDto chatRoomReqDto) {
        return chatService.createRoom(chatRoomReqDto.getName());
    }

    @GetMapping("/roomList")
    public String roomList(Model model) {
        List<ChatRoomDto> roomList = chatService.findAllRoom();
        model.addAttribute("roomList", roomList);
        return "roomList";
    }

    @GetMapping("/roomForm")
    public String roomForm() {
        return "roomForm";
    }

}