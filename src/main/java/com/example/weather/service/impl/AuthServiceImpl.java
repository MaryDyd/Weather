package com.example.weather.service.impl;

import com.example.weather.domain.UserDTO;
import com.example.weather.domain.auth.LoginRequest;
import com.example.weather.domain.auth.RegisterRequest;
import com.example.weather.domain.enums.Role;
import com.example.weather.entity.UserEntity;
import com.example.weather.exception.AuthenticationException;
import com.example.weather.exception.UserAlreadyExistsException;
import com.example.weather.repository.UserRepository;
import com.example.weather.security.jwt.JwtTokenProvider;
import com.example.weather.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        UserEntity user = userRepository.save(UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build());

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    @Override
    public UserDTO loginUser(LoginRequest request) {
        Optional<UserEntity> userOpt = Optional.ofNullable(userRepository.findByEmail(request.getEmail()));
        UserEntity user = userOpt.orElseThrow(() -> new AuthenticationException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            System.out.println("Password does not match for email: " + request.getEmail());
            throw new AuthenticationException("Invalid email or password");
        }

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    public String generateToken(UserDTO user) {
        Role role = userRepository.findByEmail(user.getEmail()).getRole();
        return jwtTokenProvider.createToken(user.getEmail(), role);
    }
}
