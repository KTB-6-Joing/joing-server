package com.ktb.joing.domain.chat.service;

import com.ktb.joing.domain.chat.dto.request.ChatMessageSocketRequest;
import com.ktb.joing.domain.chat.dto.response.ChatMessageSocketResponse;
import com.ktb.joing.domain.chat.entity.ChatMessage;
import com.ktb.joing.domain.chat.entity.ChatRoom;
import com.ktb.joing.domain.chat.entity.MessageType;
import com.ktb.joing.domain.chat.exception.ChatErrorCode;
import com.ktb.joing.domain.chat.exception.ChatException;
import com.ktb.joing.domain.chat.repository.ChatMessageRepository;
import com.ktb.joing.domain.chat.repository.ChatRoomRepository;
import com.ktb.joing.domain.notification.service.NotificationService;
import com.ktb.joing.domain.user.entity.User;
import com.ktb.joing.domain.user.exception.UserErrorCode;
import com.ktb.joing.domain.user.exception.UserException;
import com.ktb.joing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.ktb.joing.domain.chat.entity.MessageType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatSocketService {
    private static final String TOPIC_CHAT_PREFIX = "/subscribe/rooms/";
    private static final String EMPTY_CONTENT = "";

    private final SimpMessagingTemplate template;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    // 채팅방 입장 시 발생하는 메시지 처리
    public void sendEnter(String senderUserId, Long roomId){
        User senderUser = userRepository.findByUsername(senderUserId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));
        sendAndSave(ENTER, EMPTY_CONTENT, chatRoom, senderUser);
    }

    // 채팅방 퇴장 시 발생하는 메시지 처리
    public void sendLeave(String senderUserId, Long roomId){
        User senderUser = userRepository.findByUsername(senderUserId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));
        sendAndSave(LEAVE, EMPTY_CONTENT, chatRoom, senderUser);
    }

    // 채팅 전송시 발생하는 메시지 처리
    public void sendChat(String senderUserId, Long roomId, ChatMessageSocketRequest request){
        User senderUser = userRepository.findByUsername(senderUserId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));

        if(!chatRoom.containsUser(senderUser)){
            throw new ChatException(ChatErrorCode.CHAT_ROOM_ACCESS_DENIED);
        }
        sendAndSave(CHAT, request.content(), chatRoom, senderUser);
    }


    // 모든 타입의 메시지에 대한 공통 처리 로직
    private void sendAndSave(MessageType messageType, String content, ChatRoom chatRoom, User senderUser) {
        ChatMessageSocketResponse chat = new ChatMessageSocketResponse(messageType, content, senderUser, LocalDateTime.now());

        if (messageType == MessageType.CHAT) {
            sendChatNotification(chatRoom, senderUser);
        }

        template.convertAndSend(TOPIC_CHAT_PREFIX + chatRoom.getId(), chat);
        chatMessageRepository.save(new ChatMessage(chatRoom, messageType, senderUser, content));
    }

    //수신자에게 sse 알림 보내기
    private void sendChatNotification(ChatRoom chatRoom, User senderUser) {
        String notificationContent = String.format("%s님이 메시지를 보냈습니다.", senderUser.getNickname());
        String relatedUrl = "/chat/rooms/" + chatRoom.getId();
        User receiver = chatRoom.findMembers().stream()
                .filter(user -> !user.getUsername().equals(senderUser.getUsername()))
                .findFirst()
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        notificationService.send(receiver, notificationContent, relatedUrl);
    }

}
