package com.example.backend.controller;

import com.example.backend.dto.UserCredentialsDto;
import com.example.backend.dto.UserDto;
import com.example.backend.jwt.JwtUtils;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.example.backend.exceptions.NotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService service;
    private final JwtUtils jwtUtils;

    public UserController(UserService service, JwtUtils jwtUtils) {
        this.service = service;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody User user) {
        return ResponseEntity.ok(service.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserCredentialsDto dto) {
        UserDto user = service.login(dto);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password("dummy") // le mot de passe n’est pas utilisé ici
                .roles(user.getRole().toUpperCase()) // exemple : "GESTIONNAIRE", "MEMBRE"
                .build();

        String token = jwtUtils.generateToken(userDetails);

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
    // Récupérer le profil de l'utilisateur
    @GetMapping("/profile/{email}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable String email) {
        try {
            UserDto userDto = service.getUserProfile(email);
            return ResponseEntity.ok(userDto);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Mettre à jour le profil de l'utilisateur
    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateUserProfile(@RequestBody UserDto userDto) {
        try {
            UserDto updatedUser = service.updateUserProfile(userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}
