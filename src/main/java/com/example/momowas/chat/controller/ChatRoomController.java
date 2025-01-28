package com.example.momowas.chat.controller;


import com.example.momowas.authorization.CrewManager;
import com.example.momowas.chat.domain.Chat;
import com.example.momowas.chat.domain.ChatRoom;
import com.example.momowas.chat.dto.ChatDto;
import com.example.momowas.chat.dto.ChatRoomDto;
import com.example.momowas.chat.dto.ChatRoomReqDto;
import com.example.momowas.chat.service.ChatService;
import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms")
public class ChatRoomController {
    private final ChatService chatService;
    private final JwtUtil jwtUtil;
    private final CrewManager crewManager;

    @GetMapping("/{roomId}/history")
    public List<ChatDto> chatHistory(@PathVariable Long roomId) {
        return chatService.findAllChatByRoomId(roomId);
    }
    @GetMapping("/{roomId}")
    public ChatRoomDto joinRoom(@PathVariable Long roomId) {
        return chatService.findRoomById(roomId);
    }

    //모임 장이 채팅방 생성
    @PostMapping("")
    public ChatRoomDto createChatRoom(HttpServletRequest request, @RequestBody ChatRoomReqDto chatRoomReqDto) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        crewManager.hasCrewLeaderPermission(chatRoomReqDto.getCrewId(), userId);

        return chatService.createRoom(chatRoomReqDto);
    }

    @GetMapping("")
    public List<ChatRoomDto> roomList() {
        return chatService.findAllRoom();
    }

    @DeleteMapping("/{roomId}")
    public CommonResponse<String> deleteChatRoom(@PathVariable Long roomId){
        chatService.deleteChatRoom(roomId);
        return CommonResponse.of(ExceptionCode.SUCCESS,"채팅방을 삭제했습니다");
    }

}