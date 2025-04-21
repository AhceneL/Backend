package com.example.backend.service;

import com.example.backend.model.Notification;
import java.util.List;

public interface NotificationService {
    List<Notification> getNotificationsForUser(Long userId);
    List<Notification> getUnreadNotificationsForUser(Long userId);
    Notification markAsRead(Long notificationId);
    void clearNotificationsForUser(Long userId);

}
