package com.example.backend.repository;

import com.example.backend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Trouver toutes les notifications non lues pour un utilisateur
    List<Notification> findByUserIdAndIsReadFalse(Long userId);

    // Trouver toutes les notifications d'un utilisateur
    List<Notification> findByUserId(Long userId);
}
