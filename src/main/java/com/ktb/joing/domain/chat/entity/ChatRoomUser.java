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
public class ChatRoomUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String role;

    public boolean hasUser(User user) {
        if (user == null) {
            return false;
        }
        return this.user.getUsername().equals(user.getUsername());
    }

    @Builder
    public ChatRoomUser(ChatRoom chatRoom, User user, String role) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.role = role;
    }
}
