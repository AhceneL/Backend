package com.example.backend.repository;

import com.example.backend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Trouver les notifications par email de l'utilisateur assigné
    List<Notification> findByAssigneeEmail(String assigneeEmail);

    // Trouver les notifications non lues par email de l'utilisateur assigné
    List<Notification> findByAssigneeEmailAndIsReadFalse(String assigneeEmail);
}
