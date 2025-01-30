package com.ktb.joing.domain.chat.repository;

import com.ktb.joing.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Optional<ChatMessage> findFirstByChatRoomIdOrderByCreatedDateTimeDesc(Long chatRoomId);

    default Optional<ChatMessage> findRecentByChatRoomId(Long chatRoomId) {
        return findFirstByChatRoomIdOrderByCreatedDateTimeDesc(chatRoomId);
    }
}
