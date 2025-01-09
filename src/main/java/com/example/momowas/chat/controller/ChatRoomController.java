package com.example.momowas.chat.controller;


import com.example.momowas.chat.domain.Chat;
import com.example.momowas.chat.domain.ChatRoom;
import com.example.momowas.chat.dto.ChatRoomDto;
import com.example.momowas.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatService chatService;

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