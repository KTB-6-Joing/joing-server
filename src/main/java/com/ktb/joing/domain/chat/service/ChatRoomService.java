package com.ktb.joing.domain.chat.service;

import com.ktb.joing.domain.chat.dto.request.CreateChatRoomRequest;
import com.ktb.joing.domain.chat.dto.response.ChatRoomDetailResponse;
import com.ktb.joing.domain.chat.dto.response.CreateChatRoomResponse;
import com.ktb.joing.domain.chat.entity.ChatMessage;
import com.ktb.joing.domain.chat.entity.ChatRoom;
import com.ktb.joing.domain.chat.exception.ChatErrorCode;
import com.ktb.joing.domain.chat.exception.ChatException;
import com.ktb.joing.domain.chat.repository.ChatMessageRepository;
import com.ktb.joing.domain.chat.repository.ChatRoomRepository;
import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.item.exception.ItemErrorCode;
import com.ktb.joing.domain.item.exception.ItemException;
import com.ktb.joing.domain.item.repository.ItemRepository;
import com.ktb.joing.domain.user.entity.User;
import com.ktb.joing.domain.user.exception.UserErrorCode;
import com.ktb.joing.domain.user.exception.UserException;
import com.ktb.joing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ChatSocketService chatSocketService;

    // 1대1 채팅방 생성
    public CreateChatRoomResponse createChatRoom(String username, CreateChatRoomRequest request) {
        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        User otherUser = userRepository.findById(request.receiverId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        String otherRole = request.sender().equals("CREATOR") ? "PRODUCT_MANAGER" : "CREATOR";

        //이미 있는 방인지 확인
        ChatRoom chatRoom = findExistingChatRoom(username, otherUser)
                .orElseGet(() -> {
                    ChatRoom createChatRoom = ChatRoom.builder().item(item).build();
                    createChatRoom.addMember(user, request.sender()); // 유저 객체, 유저 역할
                    createChatRoom.addMember(otherUser, otherRole);
                    return chatRoomRepository.save(createChatRoom);
                });

        return new CreateChatRoomResponse(chatRoom.getId());
    }

    // 채팅방 나가기
    public void leave(String username, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() ->  new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        chatRoom.removeMember(user);

        //나갔다는 메시지 보내기
        chatSocketService.sendLeave(username, roomId);
    }

    // 자신의 채팅방 목록 출력
    public List<ChatRoomDetailResponse> findChatRooms(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        List<ChatRoom> chatRooms = chatRoomRepository.findMine(user.getUsername());
        return chatRooms.stream()
                .map(chatRoom -> toChatRoomDetail(chatRoom, user.getUsername()))
                .toList();

    }

    // 채팅방 상세정보 변환
    private ChatRoomDetailResponse toChatRoomDetail(ChatRoom chatRoom, String username) {
        Optional<ChatMessage> recentMessage = chatMessageRepository.findRecentByChatRoomId(chatRoom.getId());
        String message = recentMessage.map(ChatMessage::getContent).orElse(null);
        LocalDateTime messageCreateAt = recentMessage.map(ChatMessage::getCreatedDateTime).orElse(null);

        Optional<User> otherUser = chatRoom.findMembers().stream()
                .filter(user -> !username.equals(user.getUsername()))
                        .findAny();
        String receiverName = otherUser.map(User::getNickname)
                .orElse(null);
        String receiverProfileImage = otherUser.map(User::getProfileImage)
                .orElse(null);
        return new ChatRoomDetailResponse(chatRoom.getId(), receiverName, receiverProfileImage, message, messageCreateAt);
    }


    // 두 사용자 간의 1대1 채팅방이 이미 존재하는지 확인 -> 새로운 채팅방을 만들기 전에 중복을 방지하는 역할을 한다.
    private Optional<ChatRoom> findExistingChatRoom(String username, User otherUser) {
        List<ChatRoom> chatRooms = chatRoomRepository.findMine(username);
        return chatRooms.stream()
                .filter(chatRoom -> chatRoom.containsUser(otherUser))
                .findAny();
    }

}
