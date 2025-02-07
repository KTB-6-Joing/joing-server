package com.ktb.joing.domain.chat.entity;

import com.ktb.joing.domain.chat.exception.ChatErrorCode;
import com.ktb.joing.domain.chat.exception.ChatException;
import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;  // 기획안 정보

    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true, cascade = CascadeType.ALL)
    private final List<ChatRoomUser> chatRoomUsers = new ArrayList<>();

    public void addMember(User user,String role) {
        if (containsUser(user)) {
            throw new ChatException(ChatErrorCode.ROOM_ACCESS_DENIED);
        }
        chatRoomUsers.add(
                ChatRoomUser.builder()
                        .chatRoom(this)
                        .user(user)
                        .role(role)
                        .build()
        );
    }

    public boolean containsUser(User user) {
        return chatRoomUsers.stream()
                .anyMatch(chatRoomMember -> chatRoomMember.hasUser(user));
    }

    public void removeMember(User member) {
        chatRoomUsers.stream()
                .filter(row -> row.hasUser(member))
                .findAny()
                .ifPresent(found -> chatRoomUsers.remove(found));
    }

    public List<User> findMembers() {
        return chatRoomUsers.stream()
                .map(ChatRoomUser::getUser)
                .toList();
    }

    @Builder
    public ChatRoom(Item item) {
        this.item = item;
    }
}
