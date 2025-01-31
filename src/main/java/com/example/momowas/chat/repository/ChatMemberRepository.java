package com.example.momowas.chat.repository;

import com.example.momowas.chat.domain.ChatMember;
import com.example.momowas.chat.domain.ChatRoom;
import com.example.momowas.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

    Optional<ChatMember> findByChatRoomAndUser(ChatRoom chatRoom, User user);
    List<ChatMember> findByUser(User user);
}
