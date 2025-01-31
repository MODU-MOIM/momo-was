package com.example.momowas.chat.controller;


import com.example.momowas.chat.dto.ChatResDto;
import com.example.momowas.chat.dto.ChatRoomReqDto;
import com.example.momowas.chat.dto.ChatRoomResDto;
import com.example.momowas.chat.service.ChatRoomService;
import com.example.momowas.chat.service.ChatService;
import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.response.CommonResponse;
import com.example.momowas.response.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms")
public class ChatRoomController {
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final JwtUtil jwtUtil;

    @GetMapping("/{roomId}/history")
    public List<ChatResDto> chatHistory(HttpServletRequest request, @PathVariable Long roomId) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        return chatService.findAllChatByRoomId(userId, roomId);
    }

    @GetMapping("/{roomId}")
    public ChatRoomResDto findById(@PathVariable Long roomId) {
        return chatRoomService.findById(roomId);
    }

    @GetMapping("/me")
    public List<ChatRoomResDto> findByMe (HttpServletRequest request){
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        return chatRoomService.findChatRoomsByMe(userId);
    }

    //모임 장이 채팅방 생성
    @PostMapping("")
    public ChatRoomResDto createChatRoom(HttpServletRequest request, @RequestBody ChatRoomReqDto chatRoomReqDto) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        return chatRoomService.createRoom(userId, chatRoomReqDto);
    }

    @GetMapping("")
    public List<ChatRoomResDto> roomList() {
        return chatRoomService.findAllRoom();
    }

    @DeleteMapping("/{roomId}")
    public CommonResponse<String> deleteChatRoom(HttpServletRequest request, @PathVariable Long roomId){
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.resolveToken(request).substring(7));
        chatRoomService.deleteChatRoom(userId, roomId);
        return CommonResponse.of(ExceptionCode.SUCCESS,"채팅방을 삭제했습니다");
    }

}