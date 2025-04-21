package com.example.backend.service;

import com.example.backend.model.Notification;
import java.util.List;

public interface NotificationService {
    // Récupérer les notifications par email de l'utilisateur
    List<Notification> getNotificationsForUser(String email);

    // Récupérer les notifications non lues par email de l'utilisateur
    List<Notification> getUnreadNotificationsForUser(String email);

    // Marquer une notification comme lue
    Notification markAsRead(Long notificationId);

    // Effacer toutes les notifications pour un utilisateur par son email
    void clearNotificationsForUser(String email);
}
