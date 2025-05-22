package com.example.weather.service;


import com.example.weather.domain.UserDTO;
import com.example.weather.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDTO> findAllUsers();
    UserDTO createUser(UserDTO userDTO, String password);
    UserDTO findUserById(UUID id) throws UserNotFoundException;
    UserDTO updateUser(UUID id, UserDTO userDTO, String password) throws UserNotFoundException;
    void deleteUser(UUID id) throws UserNotFoundException;
    boolean existsByEmail(String email);
    UserDTO findByEmail(String email) throws UserNotFoundException;
}
