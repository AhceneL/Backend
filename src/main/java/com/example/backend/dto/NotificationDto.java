package com.example.backend.dto;

import java.time.LocalDateTime;

public class NotificationDto {

    private Long id;
    private String message;
    private String type;
    private boolean isRead;
    private LocalDateTime dateCreated;

    public NotificationDto(Long id, String message, String type, boolean isRead, LocalDateTime dateCreated) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.dateCreated = dateCreated;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
}
