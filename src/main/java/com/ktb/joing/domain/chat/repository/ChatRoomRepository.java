package com.ktb.joing.domain.chat.repository;

import com.ktb.joing.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 특정 사용자의 모든 채팅방을 조회
    @Query("SELECT chatRoomUser.chatRoom " +
            "FROM ChatRoomUser chatRoomUser " +
            "WHERE chatRoomUser.user.username = :username")
    List<ChatRoom> findMine(@Param("username") String username);
}
