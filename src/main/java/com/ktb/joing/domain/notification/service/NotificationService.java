package com.ktb.joing.domain.notification.service;

import com.ktb.joing.domain.notification.entity.Notification;
import com.ktb.joing.domain.notification.repository.EmitterRepository;
import com.ktb.joing.domain.notification.repository.NotificationRepository;
import com.ktb.joing.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private static final Long timeoutMillis = 60L * 1000 * 60;

    public SseEmitter subscribe(String username, String lastEventId) {
        String emitterId = makeTimeIncludeId(username);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeoutMillis));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        String eventId = makeTimeIncludeId(username);
        sendNotification(emitter, eventId,
                emitterId, "EventStream Created. [userId=%s]".formatted(username));

        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, username, emitterId, emitter);
        }

        return emitter;
    }

    public void send(User receiver, String content, String relatedUrl) {
        Notification notification = notificationRepository
                .save(createNotification(receiver, content, relatedUrl));

        String receiverId = String.valueOf(receiver.getUsername());
        String eventId = makeTimeIncludeId(receiver.getUsername());
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterByUserId(receiverId);
        emitters.forEach(
                (id, emitter) -> {
                    emitterRepository.saveEventCache(id, notification);
                    sendNotification(emitter, eventId, id,
                            NotificationConverter.toCreateNotificationDTO(notification));
                }
        );
    }

    private String makeTimeIncludeId(String userName) {
        return userName + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, String username, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository
                .findAllEventCacheByUserId(String.valueOf(username));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    private Notification createNotification(User receiver, String content, String url) {
        return Notification.builder()
                .receiver(receiver)
                .content(content)
                .relatedUrl(url)
                .isRead(false)
                .build();
    }
}
