package com.example.momowas.chat.service;

import com.example.momowas.authorization.CrewManager;
import com.example.momowas.chat.domain.Chat;
import com.example.momowas.chat.domain.ChatRoom;
import com.example.momowas.chat.domain.MessageType;
import com.example.momowas.chat.dto.ChatResDto;
import com.example.momowas.chat.dto.ChatReqDto;
import com.example.momowas.chat.repository.ChatRepository;
import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewmember.domain.Role;
import com.example.momowas.crew.service.CrewService;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final ChatMemberService chatMemberService;
    private final CrewManager crewManager;

    public ChatResDto enterChatRoomChat(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomService.findChatRoomById(chatRoomId);
        User user = userService.findUserById(userId);

        // 채팅 참여자로 등록
        chatMemberService.addParticipant(user, chatRoom);

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .senderId(user.getId())
                .senderName(user.getNickname())
                .type(MessageType.ENTER)
                .content(user.getNickname() + "님이 입장하였습니다.")
                .sendAt(LocalDateTime.now())
                .build();

        chatRepository.save(chat);
        Role role = crewManager.findUserRoleInCrew(chatRoom.getCrewId(), chat.getSenderId());
        return ChatResDto.fromEntity(chat, role);
    }

    public ChatResDto createChat(Long chatRoomId, ChatReqDto chatReqDto, Long userId) {
        ChatRoom chatRoom =  chatRoomService.findChatRoomById(chatRoomId);
        User user  = userService.findUserById(userId);

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .senderId(user.getId())
                .senderName(user.getNickname())
                .type(MessageType.TALK)
                .content(chatReqDto.getContent())
                .sendAt(LocalDateTime.now())
                .build();

        chatRepository.save(chat);
        Role role = crewManager.findUserRoleInCrew(chatRoom.getCrewId(), chat.getSenderId());
        return ChatResDto.fromEntity(chat, role);

    }


    public List<ChatResDto> findAllChatByRoomId(Long userId, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.findChatRoomById(chatRoomId);
        User user = userService.findUserById(userId);
        if(!chatMemberService.isChatMemberExists(chatRoom, user)){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }

        return chatRepository.findAllByChatRoomId(chatRoomId).stream()
                .map(chat -> {
                    Role role = crewManager.findUserRoleInCrew(chatRoom.getCrewId(), chat.getSenderId());
                    return ChatResDto.fromEntity(chat, role);
                })
                .collect(Collectors.toList());
    }



}
