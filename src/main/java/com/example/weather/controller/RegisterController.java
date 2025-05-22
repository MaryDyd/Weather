package com.example.weather.controller;

import com.example.weather.domain.UserDTO;
import com.example.weather.domain.auth.LoginRequest;
import com.example.weather.domain.auth.RegisterRequest;
import com.example.weather.service.AuthService;
import com.example.weather.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final AuthService authService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("request", new RegisterRequest());
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterRequest request) {
        authService.registerUser(request);
        return "redirect:/login";
    }
}