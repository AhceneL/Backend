package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.example.backend.model") // 🔥 POUR DÉTECTER LES ENTITÉS
@EnableJpaRepositories(basePackages = "com.example.backend.repository") // 🔥 POUR DÉTECTER LES REPOSITORIES
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
