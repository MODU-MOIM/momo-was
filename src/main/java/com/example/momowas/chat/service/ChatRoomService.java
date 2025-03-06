package com.example.momowas.chat.service;

import com.example.momowas.authorization.CrewManager;
import com.example.momowas.chat.domain.ChatRoom;
import com.example.momowas.chat.dto.ChatRoomReqDto;
import com.example.momowas.chat.dto.ChatRoomResDto;
import com.example.momowas.chat.repository.ChatRoomRepository;
import com.example.momowas.crew.service.CrewService;
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
    private final ChatMemberService chatMemberService;
    private final UserService userService;
    private final CrewManager crewManager;
    private final CrewService crewService;

    public List<ChatRoomResDto> findAllRoom() {
        return chatRoomRepository.findAll().stream()
                .map(chatRoom -> ChatRoomResDto.fromEntity(
                        chatRoom,
                        crewService.findCrewById(chatRoom.getCrewId())
                ))
                .collect(Collectors.toList());
    }


    public ChatRoom findChatRoomById(Long chatRoomId){
        return chatRoomRepository.findById(chatRoomId).orElseThrow(()->new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));
    }

    public ChatRoomResDto findById(Long chatRoomId) {
        ChatRoom chatRoom =  chatRoomRepository.findById(chatRoomId).orElseThrow(()->new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));

        return ChatRoomResDto.fromEntity(chatRoom, crewService.findCrewById(chatRoom.getCrewId()));
    }

    public List<ChatRoomResDto> findChatRoomsByMe(Long userId) {
        User user = userService.findUserById(userId);
        List<ChatRoom> chatRooms = chatMemberService.findChatRoomsByUser(user);

        return chatRooms.stream()
                .map(chatRoom -> ChatRoomResDto.fromEntity(
                        chatRoom,
                        crewService.findCrewById(chatRoom.getCrewId())
                ))
                .collect(Collectors.toList());
    }


    public ChatRoomResDto createRoom(Long userId, ChatRoomReqDto chatRoomReqDto) {
        User user  = userService.findUserById(userId);
        if(!crewManager.hasCrewLeaderPermission(chatRoomReqDto.getCrewId(), user.getId())){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }

        ChatRoom chatRoom  = ChatRoom.builder()
                .crewId(chatRoomReqDto.getCrewId())
                .name(chatRoomReqDto.getName())
                .createdAt(LocalDateTime.now())
                .build();
        chatRoomRepository.save(chatRoom);

        // 채팅 참여자로 추가
        chatMemberService.addParticipant(user, chatRoom);

        return ChatRoomResDto.fromEntity(chatRoom, crewService.findCrewById(chatRoom.getCrewId()));
    }

    @Transactional
    public void deleteChatRoom(Long userId, Long chatRoomId){
        ChatRoom chatRoom =  chatRoomRepository.findById(chatRoomId).orElseThrow(()->new BusinessException(ExceptionCode.CHATROOM_NOT_FOUND));
        if(!crewManager.hasCrewLeaderPermission(chatRoom.getCrewId(),userId)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }

        chatRoomRepository.delete(chatRoom);
    }
}
