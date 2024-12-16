package com.ktb.joing.domain.notification.repository;

import com.ktb.joing.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
