package com.example.backend.controller;

import com.example.backend.model.Notification;
import com.example.backend.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")  // Remplacez cette URL avec celle de votre frontend si nécessaire
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Obtenir toutes les notifications d'un utilisateur par son email
    @GetMapping("/user/{email}")
    public ResponseEntity<List<Notification>> getNotificationsForUser(@PathVariable String email) {
        List<Notification> notifications = notificationService.getNotificationsForUser(email);
        return ResponseEntity.ok(notifications);
    }

    // Obtenir les notifications non lues pour un utilisateur par son email
    @GetMapping("/user/{email}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotificationsForUser(@PathVariable String email) {
        List<Notification> notifications = notificationService.getUnreadNotificationsForUser(email);
        return ResponseEntity.ok(notifications);
    }

    // Marquer une notification comme lue
    @PutMapping("/{notificationId}")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long notificationId) {
        Notification notification = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(notification);
    }

    // Effacer toutes les notifications pour un utilisateur par email
    @DeleteMapping("/user/{email}")
    public ResponseEntity<Void> clearNotificationsForUser(@PathVariable String email) {
        notificationService.clearNotificationsForUser(email);
        return ResponseEntity.noContent().build();
    }
}
