package com.example.backend.impl;

import com.example.backend.dto.UserCredentialsDto;
import com.example.backend.dto.UserDto;
import com.example.backend.exceptions.NotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDto register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return toDto(repo.save(user));
    }

    @Override
    public UserDto login(UserCredentialsDto credentials) {
        Optional<User> optionalUser = repo.findByEmail(credentials.getEmail());
        if (optionalUser.isEmpty() || !encoder.matches(credentials.getPassword(), optionalUser.get().getPassword())) {
            throw new NotFoundException("Email ou mot de passe invalide");
        }
        return toDto(optionalUser.get());
    }

    @Override
    public UserDto getById(Long id) {
        return repo.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé"));
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setRole(user.getRole());
        return dto;
    }
}
