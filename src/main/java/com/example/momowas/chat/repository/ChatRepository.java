package com.example.momowas.chat.repository;

import com.example.momowas.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findAllByChatRoomId(Long roomId);
    Chat findFirstByChatRoomIdOrderBySendAtDesc(Long roomId);
}