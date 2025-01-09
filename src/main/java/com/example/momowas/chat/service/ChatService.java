package com.example.momowas.chat.service;

import com.example.momowas.chat.domain.ChatRoom;
import com.example.momowas.chat.dto.ChatRoomDto;
import com.example.momowas.chat.repository.ChatRepository;
import com.example.momowas.chat.repository.ChatRoomRepository;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    public List<ChatRoomDto> findAllRoom() {
        return chatRoomRepository.findAll().stream()
                .map(ChatRoomDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ChatRoomDto findRoomById(Long id) {
        ChatRoom chatRoom =  chatRoomRepository.findById(id).orElseThrow(()->new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));
        return ChatRoomDto.fromEntity(chatRoom);
    }

    public ChatRoomDto createRoom(String name) {
        ChatRoom chatRoom  = ChatRoom.builder()
                .name(name)
                .createdAt(LocalDateTime.now())
                .build();
        return ChatRoomDto.fromEntity(chatRoom);
    }

}
