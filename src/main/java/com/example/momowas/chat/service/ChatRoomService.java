package com.example.momowas.chat.service;

import com.example.momowas.authorization.CrewManager;
import com.example.momowas.chat.domain.ChatRoom;
import com.example.momowas.chat.dto.ChatRoomReqDto;
import com.example.momowas.chat.dto.ChatRoomResDto;
import com.example.momowas.chat.repository.ChatRoomRepository;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    private final CrewManager crewManager;


    public List<ChatRoomResDto> findAllRoom() {
        return chatRoomRepository.findAll().stream()
                .map(ChatRoomResDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ChatRoom findChatRoomById(Long chatRoomId){
        return chatRoomRepository.findById(chatRoomId).orElseThrow(()->new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));
    }
    public ChatRoomResDto findRoomById(Long chatRoomId) {
        ChatRoom chatRoom =  chatRoomRepository.findById(chatRoomId).orElseThrow(()->new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));
        return ChatRoomResDto.fromEntity(chatRoom);
    }

    public ChatRoomResDto createRoom(Long userId, ChatRoomReqDto chatRoomReqDto) {
        User user  = userService.findUserById(userId);
        crewManager.hasCrewLeaderPermission(chatRoomReqDto.getCrewId(), user.getId());

        ChatRoom chatRoom  = ChatRoom.builder()
                .crewId(chatRoomReqDto.getCrewId())
                .name(chatRoomReqDto.getName())
                .createdAt(LocalDateTime.now())
                .build();
        chatRoomRepository.save(chatRoom);
        return ChatRoomResDto.fromEntity(chatRoom);
    }

    @Transactional
    public void deleteChatRoom(Long userId, Long chatRoomId){
        ChatRoom chatRoom =  chatRoomRepository.findById(chatRoomId).orElseThrow(()->new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));
        crewManager.hasCrewLeaderPermission(chatRoom.getCrewId(), userId);

        chatRoomRepository.delete(chatRoom);
    }
}
