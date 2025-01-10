package com.ktb.joing.domain.notification.service;

import com.ktb.joing.domain.notification.dto.response.NotificationResponse;
import com.ktb.joing.domain.notification.entity.Notification;

public class NotificationConverter {
    public static NotificationResponse toCreateNotificationDTO(Notification notification){
        return NotificationResponse.from(notification);
    }
}
