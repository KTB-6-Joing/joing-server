package com.ktb.joing.domain.chat.entity;

import com.ktb.joing.common.model.BaseTimeEntity;
import com.ktb.joing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User senderUser;

    @Column(length = 2000)
    private String content;

    @Builder
    public ChatMessage(ChatRoom chatRoom, MessageType messageType, User senderUser, String content) {
        this.chatRoom = chatRoom;
        this.messageType = messageType;
        this.senderUser = senderUser;
        this.content = content;
    }

}
