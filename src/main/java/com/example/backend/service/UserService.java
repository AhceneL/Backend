package com.example.backend.service;

import com.example.backend.dto.UserCredentialsDto;
import com.example.backend.dto.UserDto;
import com.example.backend.model.User;

public interface UserService {
    UserDto register(User user);
    UserDto login(UserCredentialsDto credentials);
    UserDto getById(Long id);
}
