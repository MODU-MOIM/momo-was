package com.example.momowas.chat.service;

import com.example.momowas.chat.domain.ChatMember;
import com.example.momowas.chat.domain.ChatRoom;
import com.example.momowas.chat.repository.ChatMemberRepository;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMemberService {
    private final ChatMemberRepository chatMemberRepository;

    @Transactional
    public void addParticipant(User user, ChatRoom chatRoom) {
        boolean alreadyJoined = chatMemberRepository.findByChatRoomAndUser(chatRoom, user).isPresent();
        if (alreadyJoined) {
           throw new BusinessException(ExceptionCode.ALREADY_ENTER);
        }
        ChatMember chatMember = ChatMember.builder()
                .chatRoom(chatRoom)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build();
        chatMemberRepository.save(chatMember);

    }

    @Transactional(readOnly = true)
    public List<ChatRoom> findChatRoomsByUser(User user) {
        return chatMemberRepository.findByUser(user).stream()
                .map(ChatMember::getChatRoom)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isChatMemberExists(ChatRoom chatRoom, User user) {
        return chatMemberRepository.existsByChatRoomAndUser(chatRoom,user);
    }

}
