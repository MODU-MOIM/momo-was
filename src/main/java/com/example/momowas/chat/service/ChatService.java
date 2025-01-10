package com.example.momowas.chat.service;

import com.example.momowas.chat.domain.Chat;
import com.example.momowas.chat.domain.ChatRoom;
import com.example.momowas.chat.domain.MessageType;
import com.example.momowas.chat.dto.ChatDto;
import com.example.momowas.chat.dto.ChatReqDto;
import com.example.momowas.chat.dto.ChatRoomDto;
import com.example.momowas.chat.repository.ChatRepository;
import com.example.momowas.chat.repository.ChatRoomRepository;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.dto.UserDto;
import com.example.momowas.user.service.UserService;
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
    private final UserService userService;

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
        chatRoomRepository.save(chatRoom);
        return ChatRoomDto.fromEntity(chatRoom);
    }

    public ChatDto createChat(Long roomId, ChatReqDto chatReqDto, Long userId) {

        ChatRoom chatRoom =  chatRoomRepository.findById(roomId).orElseThrow(()->new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));
        User user  = userService.readById(userId);

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .senderId(userId)
                .senderName(user.getNickname())
                .type(MessageType.TALK)
                .content(chatReqDto.getContent())
                .sendAt(LocalDateTime.now())
                .build();

        chatRepository.save(chat);
        return ChatDto.fromEntity(chat);

    }
    public List<ChatDto> findAllChatByRoomId(Long roomId) {
        ChatRoom chatRoom =  chatRoomRepository.findById(roomId).orElseThrow(()->new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));

        return chatRepository.findAllByChatRoomId(roomId).stream()
                .map(ChatDto::fromEntity)
                .collect(Collectors.toList());
    }

}
