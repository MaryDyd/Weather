package com.example.weather.service;

import com.example.weather.domain.UserDTO;
import com.example.weather.domain.auth.LoginRequest;
import com.example.weather.domain.auth.RegisterRequest;

public interface AuthService {
    UserDTO loginUser(LoginRequest request);

    UserDTO registerUser(RegisterRequest request);
    String generateToken(UserDTO user);
}
