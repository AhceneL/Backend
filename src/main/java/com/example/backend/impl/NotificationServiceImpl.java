package com.example.backend.impl;

import com.example.backend.model.Notification;
import com.example.backend.repository.NotificationRepository;
import com.example.backend.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> getNotificationsForUser(String email) {
        return notificationRepository.findByAssigneeEmail(email);  // Recherche par email au lieu de userId
    }

    @Override
    public List<Notification> getUnreadNotificationsForUser(String email) {
        return notificationRepository.findByAssigneeEmailAndIsReadFalse(email);  // Recherche par email pour notifications non lues
    }

    @Override
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public void clearNotificationsForUser(String email) {
        List<Notification> notifications = notificationRepository.findByAssigneeEmail(email);
        notificationRepository.deleteAll(notifications);
    }
}
